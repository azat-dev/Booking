import 'reflect-metadata';

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import RouteItem from "./presentation/app/router/RouteItem.ts";
import PageMain from "./presentation/pages/page-main/PageMain.tsx";
import PageUserProfile from "./presentation/pages/page-user-profile/PageUserProfile.tsx";
import PrivateRoute from "./presentation/app/router/PrivateRoute.tsx";
import LocalAuthDataConfig from "./presentation/app/config/data/LocalAuthDataConfig.ts";
import DomainConfig from "./presentation/app/config/domain/DomainConfig.ts";
import GuestComponentsConfig from "./presentation/app/config/presentation/guest/GuestComponentsConfig.ts";
import DialogsStore from "./presentation/stores/DialogsStore.ts";
import CommonPagesConfig from "./presentation/app/config/presentation/common/CommonPagesConfig.ts";
import CommonDialogsConfig from "./presentation/app/config/presentation/common/CommonDialogsConfig.ts";
import App from "./presentation/app/App.tsx";
import RouterVM from "./presentation/app/router/RouterVM.tsx";
import AppVM from "./presentation/app/AppVM.tsx";
import PublicRoute from "./presentation/app/router/PublicRoute.tsx";
import useLoadedValue from "./presentation/app/router/useLoadedValue.ts";
import IdentityPolicies from "./presentation/app/config/domain/IdentityPolicies.ts";
import AppSessionImpl from "./domain/auth/entities/AppSessionImpl.ts";
import BusImpl from "./domain/utils/BusImpl.ts";
import IdentityCommands from "./presentation/app/config/domain/IdentityCommands.ts";
import AppStarted from './domain/auth/events/AppStarted.ts';
import CommonDialogsCommands from "./presentation/app/config/presentation/common/CommonDialogsCommands.ts";
import IdentityDataConfig from "./presentation/app/config/data/IdentityDataConfig.ts";
import singleton from "./utils/singleton.ts";
import {useNavigate, useParams, useSearchParams} from "react-router-dom";
import AppSessionAuthenticated from "./domain/auth/entities/AppSessionAuthenticated.ts";


const buildApp = (baseApiUrl: string) => {

    const bus = new BusImpl();

    const appSession = new AppSessionImpl(bus)

    const localAuthDataConfig = new LocalAuthDataConfig();

    const identityDataConfig = new IdentityDataConfig(
        baseApiUrl,
        localAuthDataConfig.localAuthDataRepository()
    );

    const identityCommands = new IdentityCommands(
        appSession,
        identityDataConfig.localAuthService(),
        identityDataConfig.userInfoService(),
        bus.publish
    );


    const domainConfig = new DomainConfig(bus);

    domainConfig.registerPolicies(
        new IdentityPolicies(
            appSession,
            localAuthDataConfig.localAuthDataRepository(),
            identityCommands,
            bus.publish
        )
    );


    const dialogs = new CommonDialogsConfig(
        identityCommands
    );

    const activeDialogStore = new DialogsStore(dialogs, bus);

    const commonDialogsCommands = new CommonDialogsCommands(
        activeDialogStore.activeDialog,
        dialogs,
        bus.publish
    )

    dialogs.commonDialogsCommands = commonDialogsCommands;


    const guestComponents = new GuestComponentsConfig(
        identityCommands,
        commonDialogsCommands,
        appSession,
        bus,
    );

    bus.subscribe(async (event) => {
        activeDialogStore.handle(event);
    });

    const commonPagesConfig = new CommonPagesConfig(
        identityCommands,
        guestComponents,
        bus
    );

    const guestPages = singleton(async () => {

        const GuestPagesConfig = (await import("./presentation/app/config/presentation/guest/GuestPagesConfig.ts")).default;

        return new GuestPagesConfig(
            guestComponents,
            bus
        );
    });

    const hostingPages = singleton(async () => {

        const HostingsModule = (await import("./application/HostingModule.ts")).default;
        return (session: AppSessionAuthenticated) => {
            return HostingsModule.make(
                baseApiUrl,
                session,
                localAuthDataConfig,
                identityCommands,
                commonDialogsCommands,
                domainConfig
            );
        }
    });

    const routes: RouteItem[] = [
        new PublicRoute(
            "/",
            () => {

                const vm = useLoadedValue(() => guestPages().then(p => p.mainPage()), []);
                if (!vm) {
                    return null;
                }

                return <PageMain vm={vm}/>;
            }
        ),
        new PrivateRoute(
            "/profile",
            "/",
            () => null,
            ({session}) => {
                const vm = useLoadedValue(() => commonPagesConfig.profilePage(session), [session]);
                if (!vm) {
                    return null;
                }

                return <PageUserProfile vm={vm}/>;
            }
        ),
        new PrivateRoute(
            "/listings",
            "/",
            () => null,
            ({session}) => {

                const navigate = useNavigate();

                const page = useLoadedValue(async () => {
                    const pagesFactory = await hostingPages();
                    const pages = await pagesFactory(session);
                    return pages.listingsPage(
                        (listingId) => {
                            navigate(`/listings/become-host/${listingId.val}`);
                        },
                        () => {
                            navigate(`/listings/become-host`);
                        }
                    );

                }, [session]);

                if (!page) {
                    return null;
                }

                return page;
            }
        ),
        new PrivateRoute(
            "/listings/become-host/:listingId?/:step?",
            "/",
            () => null,
            ({session}) => {
                const {step, listingId} = useParams();
                const navigate = useNavigate();


                const page = useLoadedValue(async () => {

                    const updateParams = (step: string | null, listingId: string | null) => {
                        if (listingId && step) {
                            navigate(`/listings/become-host/${listingId}/${step}`);
                            return;
                        }

                        if (listingId) {
                            navigate(`/listings/become-host/${listingId}`);
                            return;
                        }

                        if (step) {
                            navigate(`/listings/become-host/${step}`);
                            return;
                        }

                        navigate(`/listings/become-host`);
                    }

                    const pagesFactory = await hostingPages();
                    const pages = await pagesFactory(session);

                    return pages.editListingPage(
                        listingId ?? null,
                        step ?? null,
                        updateParams
                    );

                }, [session, listingId, step, navigate]);
                if (!page) {
                    return null;
                }

                return page;
            }
        )
    ];

    const router = new RouterVM(routes, appSession);

    guestComponents.navigation = {
        openUserProfilePage() {
            router.navigate("/profile");
        }
    }

    const app = new AppVM(router, activeDialogStore.activeDialog);
    bus.publish(new AppStarted());

    return app;
}

console.log("API URL", import.meta.env.VITE_API_URL);
const app = buildApp(`${import.meta.env.VITE_API_URL}`);

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);


root.render(
    <React.StrictMode>
        <App vm={app}/>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

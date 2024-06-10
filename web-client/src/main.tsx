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
import PresentationConfig from "./presentation/app/config/presentation/PresentationConfig.ts";
import App from "./presentation/app/App.tsx";
import RouterVM from "./presentation/app/router/RouterVM.tsx";
import AppVM from "./presentation/app/AppVM.tsx";
import PublicRoute from "./presentation/app/router/PublicRoute.tsx";
import useLoadedValue from "./presentation/app/router/useLoadedValue.ts";
import IdentityPolicies from "./presentation/app/config/domain/IdentityPolicies.ts";
import AppSessionImpl from "./domain/auth/entities/AppSessionImpl.ts";
import BusImpl from "./domain/utils/BusImpl.ts";
import IdentityCommandHandlers from "./presentation/app/config/domain/IdentityCommandHandlers.ts";
import AppStarted from './domain/auth/events/AppStarted.ts';
import ProfileCommandsHandlersProvider from "./presentation/app/config/presentation/ProfileCommandsHandlersProvider.ts";
import DialogsCommandHandlersProvider
    from "./presentation/app/config/presentation/common/DialogsCommandHandlersProvider.ts";
import IdentityDataConfig from "./presentation/app/config/data/IdentityDataConfig.ts";
import singleton from "./utils/singleton.ts";


const buildApp = (baseApiUrl: string) => {

    const bus = new BusImpl();

    const appSession = new AppSessionImpl(bus)

    const localAuthDataConfig = new LocalAuthDataConfig();

    const identityDataConfig = new IdentityDataConfig(
        baseApiUrl,
        localAuthDataConfig.localAuthDataRepository()
    );

    const domainPoliciesConfig = new IdentityPolicies(
        appSession,
        localAuthDataConfig.localAuthDataRepository(),
        bus
    );

    const identityCommandHandlersConfig = new IdentityCommandHandlers(
        appSession,
        bus,
        identityDataConfig.localAuthService(),
        identityDataConfig.userInfoService()
    );


    const domainConfig = new DomainConfig(bus);
    domainConfig.addCommandHandlersProvider(identityCommandHandlersConfig);
    domainConfig.addPoliciesProvider(domainPoliciesConfig);


    const guestComponents = new GuestComponentsConfig(appSession, bus);

    const dialogs = new CommonDialogsConfig(bus);

    const activeDialogStore = new DialogsStore(dialogs, bus);

    bus.subscribe(async (event) => {
        activeDialogStore.handle(event);
    });

    const commonPagesConfig = new CommonPagesConfig(
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
        return HostingsModule.make(
            baseApiUrl,
            appSession,
            localAuthDataConfig,
            domainConfig
        );
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
                const page = useLoadedValue(() => hostingPages().then(p => p.listingsPage(session)), [session]);
                if (!page) {
                    return null;
                }

                return page;
            }
        ),
        new PrivateRoute(
            "/listings/add-new",
            "/",
            () => null,
            ({session}) => {
                const page = useLoadedValue(() => hostingPages().then(p => p.editListingPage(session)), [session]);
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

    const profileCommandsHandlersProvider = new ProfileCommandsHandlersProvider(
        appSession,
        bus
    );

    const dialogsCommandHandlersProvider = new DialogsCommandHandlersProvider(
        activeDialogStore.activeDialog,
        dialogs,
        bus
    );

    const presentationConfig = new PresentationConfig(bus);

    presentationConfig.addCommandHandlersProvider(profileCommandsHandlersProvider);
    presentationConfig.addCommandHandlersProvider(dialogsCommandHandlersProvider);


    const app = new AppVM(router, activeDialogStore.activeDialog);
    bus.publish(new AppStarted());

    return app;
}


const app = buildApp("http://localhost:8080");

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

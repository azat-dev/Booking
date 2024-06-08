import 'reflect-metadata';

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import RouteItem from "./presentation/app/router/RouteItem.ts";
import PageMain from "./presentation/pages/page-main/PageMain.tsx";
import PageUserProfile from "./presentation/pages/page-user-profile/PageUserProfile.tsx";
import PrivateRoute from "./presentation/app/router/PrivateRoute.tsx";
import DataConfig from "./presentation/app/config/DataConfig.ts";
import DomainConfig from "./presentation/app/config/DomainConfig.ts";
import ComponentsConfig from "./presentation/app/config/ComponentsConfig.ts";
import DialogsConfig from "./presentation/app/config/DialogsConfig.ts";
import DialogsStore from "./presentation/stores/DialogsStore.ts";
import PagesConfig from "./presentation/app/config/PagesConfig.ts";
import PresentationConfig from "./presentation/app/config/PresentationConfig.ts";
import App from "./presentation/app/App.tsx";
import RouterVM from "./presentation/app/router/RouterVM.tsx";
import AppVM from "./presentation/app/AppVM.tsx";
import PublicRoute from "./presentation/app/router/PublicRoute.tsx";
import useLoadedValue from "./presentation/app/router/useLoadedValue.ts";
import DomainPoliciesConfig from "./presentation/app/config/DomainPoliciesConfig.ts";
import AppSessionImpl from "./domain/auth/entities/AppSessionImpl.ts";
import BusImpl from "./domain/utils/BusImpl.ts";
import DomainCommandHandlersConfig from "./presentation/app/config/DomainCommandHandlersConfig.ts";
import AppStarted from './domain/auth/events/AppStarted.ts';
import PresentationCommandHandlersConfig from "./presentation/app/config/PresentationCommandHandlersConfig.ts";
import DialogsCommandHandlersConfig from "./presentation/app/config/DialogsCommandHandlersConfig.ts";
import PageListings from "./presentation/pages/page-listings/PageListings.tsx";


const buildApp = (baseApiUrl: string) => {

    const dataConfig = new DataConfig(baseApiUrl);

    const bus = new BusImpl();

    const appSession = new AppSessionImpl(bus)

    const domainPoliciesConfig = new DomainPoliciesConfig(
        appSession,
        dataConfig.localAuthDataRepository(),
        bus
    );

    const domainCommandHandlersConfig = new DomainCommandHandlersConfig(
        appSession,
        bus,
        dataConfig.localAuthService(),
        dataConfig.userInfoService(),
        domainPoliciesConfig
    );

    const domainConfig = new DomainConfig(
        domainPoliciesConfig,
        domainCommandHandlersConfig,
        bus
    );


    const components = new ComponentsConfig(appSession, bus);

    const dialogs = new DialogsConfig(bus);

    const activeDialogStore = new DialogsStore(dialogs, bus);

    bus.subscribe(async (event) => {
        activeDialogStore.handle(event);
    });

    const pagesConfig = new PagesConfig(components, bus);

    const routes: RouteItem[] = [
        new PublicRoute(
            "/",
            () => {
                const vm = useLoadedValue(() => pagesConfig.mainPage(), []);
                if (!vm) {
                    return <h1>Loading...</h1>;
                }

                return <PageMain vm={vm}/>;
            }
        ),
        new PrivateRoute(
            "/profile",
            "/",
            () => <h1>Authenticating...</h1>,
            ({session}) => {
                const vm = useLoadedValue(() => pagesConfig.profilePage(session), [session]);
                if (!vm) {
                    return <h1>Loading...</h1>;
                }

                return <PageUserProfile vm={vm}/>;
            }
        ),
        new PrivateRoute(
            "/listings",
            "/",
            () => <h1>Authenticating...</h1>,
            ({session}) => {
                const vm = useLoadedValue(() => pagesConfig.listingsPage(session), [session]);
                if (!vm) {
                    return <h1>Loading...</h1>;
                }

                return <PageListings vm={vm}/>;
            }
        )
    ];

    const router = new RouterVM(routes, appSession, pagesConfig);

    components.navigation = {
        openUserProfilePage() {
            router.navigate("/profile");
        }
    }

    const presentationHandlersConfig = new PresentationCommandHandlersConfig(
        appSession,
        bus
    )

    new PresentationConfig(presentationHandlersConfig, bus);

    new DialogsCommandHandlersConfig(
        activeDialogStore.activeDialog,
        dialogs,
        bus
    );

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

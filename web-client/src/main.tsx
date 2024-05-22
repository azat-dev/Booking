import 'reflect-metadata';

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import RouteItem from "./presentation/app/router/RouteItem.ts";
import PageMain from "./presentation/pages/page-main/PageMain.tsx";
import PageUserProfile from "./presentation/pages/page-user-profile/PageUserProfile.tsx";
import PrivateRoute from "./presentation/app/router/PrivateRoute.tsx";
import DataModule from "./DataModule.ts";
import DomainModule from "./presentation/app/app-model/DomainModule.ts";
import ComponentsModule from "./presentation/app/app-model/ComponentsModule.ts";
import DialogsModule from "./presentation/app/app-model/DialogsModule.ts";
import DialogsStore from "./presentation/stores/DialogsStore.ts";
import PagesConfig from "./PagesConfig.ts";
import OpenFileDialogForUploadingUserPhoto from "./presentation/commands/OpenFileDialogForUploadingUserPhoto.ts";
import PresentationModule from "./presentation/app/app-model/PresentationModule.ts";
import App from "./presentation/app/App.tsx";
import RouterVM from "./presentation/app/router/RouterVM.tsx";
import AppVM from "./presentation/app/AppVM.tsx";
import PublicRoute from "./presentation/app/router/PublicRoute.tsx";
import useLoadedValue from "./presentation/app/router/useLoadedValue.ts";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const dataModule = new DataModule("http://localhost:8080");

const domainModule = new DomainModule(
    dataModule.localAuthDataRepository(),
    dataModule.authService(),
    dataModule.userInfoService()
);

const componentsModule = new ComponentsModule(
    domainModule.appSession,
    domainModule.bus
);

const dialogsModule = new DialogsModule(
    domainModule.bus
);

const dialogsStore = new DialogsStore(dialogsModule, domainModule.bus);
domainModule.bus.subscribe(async (event) => {
    dialogsStore.handle(event);
});

const pagesModule = new PagesConfig(componentsModule, domainModule.bus);

// const accommodationsRegistry = new AccommodationsRegistryImpl();
//
// const reservationService = new ReservationServiceImpl();


const routes: RouteItem[] = [
    new PublicRoute(
        "/",
        () => {
            const vm = useLoadedValue(() => pagesModule.mainPage(), []);
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
            const vm = useLoadedValue(() => pagesModule.profilePage(session), [session]);
            if (!vm) {
                return <h1>Loading...</h1>;
            }

            return <PageUserProfile vm={vm}/>;
        }
    )
];

const router = new RouterVM(routes, domainModule.appSession);

const app = new AppVM(router);

domainModule.bus.subscribe(async (event) => {
    if (!event.isCommand) {
        return;
    }

    switch (event.type) {
        // case OpenUserProfilePage.type:
        //     app.runProfilePage();
        //     break;
        case OpenFileDialogForUploadingUserPhoto.type:
            break;
    }
});

new PresentationModule(
    domainModule.appSession,
    domainModule.bus
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

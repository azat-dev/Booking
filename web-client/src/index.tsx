import 'reflect-metadata';

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import App from "./presentation/app/App";
import AccommodationsRegistryImpl from "./domain/accommodations/AccommodationsRegistryImpl";
import ReservationServiceImpl from "./domain/booking/ReservationServiceImpl";
import DataModule from "./DataModule";
import DomainModule from "./presentation/app/app-model/DomainModule";
import ComponentsModule from "./presentation/app/app-model/ComponentsModule";
import DialogsModule from "./presentation/app/app-model/DialogsModule";
import PagesModule from "./PagesModule";
import AnonymousAppVM from "./presentation/app/app-model/AnonymousAppVM";
import DialogsStore from "./presentation/stores/DialogsStore";
import OpenUserProfilePage from "./presentation/commands/OpenUserProfilePage";

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

const pagesModule = new PagesModule(componentsModule);

const accommodationsRegistry = new AccommodationsRegistryImpl();

const reservationService = new ReservationServiceImpl();

const appVm = new AnonymousAppVM(
    dialogsStore,
    pagesModule,
    domainModule.bus,
);

domainModule.bus.subscribe(async (event) => {
    if (!event.isCommand) {
        return;
    }

    switch (event.type) {
        case OpenUserProfilePage.TYPE:
            appVm.runProfilePage();
            break;
    }
});

root.render(
    <React.StrictMode>
        <App vm={appVm}/>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

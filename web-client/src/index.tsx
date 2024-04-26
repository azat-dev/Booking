import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import AppViewModelImpl from "./presentation/app/AppViewModelImpl";
import App from "./presentation/app/App";
import LocalStorageTokensRepository from "./LocalStorageTokensRepository";
import AuthServiceImpl from "./data/auth/services/AuthServiceImpl";
import CurrentSessionStoreImpl from "./domain/auth/CurrentSession/CurrentSessionStoreImpl";
import AccommodationsRegistryImpl from "./domain/accommodations/AccommodationsRegistryImpl";
import ReservationServiceImpl from "./domain/booking/ReservationServiceImpl";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const tokensRepository = new LocalStorageTokensRepository();
const authService = new AuthServiceImpl();

const currentSession = new CurrentSessionStoreImpl(
    tokensRepository,
    authService
);

const accommodationsRegistry = new AccommodationsRegistryImpl();

const reservationService = new ReservationServiceImpl();

const vm = new AppViewModelImpl(
    currentSession,
    accommodationsRegistry,
    reservationService
);

root.render(
    <React.StrictMode>
        <App vm={vm} />
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import AppViewModelImpl from "./presentation/app/app-model/AppViewModelImpl";
import App from "./presentation/app/App";
import LocalAuthDataRepositoryImpl from "./LocalAuthDataRepositoryImpl";
import AuthServiceImpl from "./data/auth/services/AuthServiceImpl";
import CurrentSessionStoreImpl from "./domain/auth/CurrentSession/CurrentSessionStoreImpl";
import AccommodationsRegistryImpl from "./domain/accommodations/AccommodationsRegistryImpl";
import ReservationServiceImpl from "./domain/booking/ReservationServiceImpl";
import { Configuration, DefaultApi } from "./data/API";
import PersonalUserInfoService from "./domain/auth/CurrentSession/Session/PersonalUserInfoService";
import UserInfo from "./domain/auth/values/User";
import FullName from "./domain/auth/CurrentSession/Session/FullName";
import FirstName from "./domain/auth/CurrentSession/Session/FirstName";
import Email from "./domain/auth/values/Email";
import LastName from "./domain/auth/CurrentSession/Session/LastName";
import UserInfoServiceImpl from "./data/auth/services/UserInfoServiceImpl";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const localAuthDataRespository = new LocalAuthDataRepositoryImpl();

const apiConfig: Configuration = new Configuration({
    basePath: "http://localhost:8080",
    accessToken: async () => {
        const authData = await localAuthDataRespository.get();
        return authData?.accessToken.val ?? "";

    }
});

const api = new DefaultApi(apiConfig);
const authService = new AuthServiceImpl(api, localAuthDataRespository);

const userInfoService: PersonalUserInfoService = new UserInfoServiceImpl(api);

const currentSessionStore = new CurrentSessionStoreImpl(
    authService,
    userInfoService,
    localAuthDataRespository
);

currentSessionStore.tryToLoadLastSession();

console.log("Current session", currentSessionStore.current.value);
currentSessionStore.current.listen((session) => {
    console.log("Current session changed", session);
});

const accommodationsRegistry = new AccommodationsRegistryImpl();

const reservationService = new ReservationServiceImpl();

const vm = new AppViewModelImpl(
    currentSessionStore,
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

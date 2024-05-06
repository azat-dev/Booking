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
import { Configuration, DefaultApi } from "./data/API";
import UserInfoService from "./domain/auth/CurrentSession/Session/UserInfoService";
import UserInfo from "./domain/auth/values/User";
import FullName from "./domain/auth/CurrentSession/Session/FullName";
import FirstName from "./domain/auth/CurrentSession/Session/FirstName";
import Email from "./domain/auth/values/Email";
import LastName from "./domain/auth/CurrentSession/Session/LastName";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const tokensRepository = new LocalStorageTokensRepository();

const apiConfig: Configuration = new Configuration({
    basePath: "http://localhost:8080",
});

const api = new DefaultApi(apiConfig);
const authService = new AuthServiceImpl(api, tokensRepository);

const userInfoService: UserInfoService = {
    getUserInfo: async (userId): Promise<UserInfo> => {
        return {
            id: userId,
            email: new Email("some-email"),
            fullName: new FullName(new FirstName("Some"), new LastName("Name")),
            avatar: null,
        };
    },
};

const currentSession = new CurrentSessionStoreImpl(
    authService,
    userInfoService,
    tokensRepository
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

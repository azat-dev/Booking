import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import AppViewModelImpl from "./presentation/app/AppViewModelImpl";
import App from "./presentation/app/App";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const vm = new AppViewModelImpl();

root.render(
    <React.StrictMode>
        <App vm={vm} />
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

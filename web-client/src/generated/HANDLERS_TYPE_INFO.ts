import LoginByEmailHandler from "../domain/auth/handlers/LoginByEmailHandler";
import LoginByTokenHandler from "../domain/auth/handlers/LoginByTokenHandler";
import LogoutHandler from "../domain/auth/handlers/LogoutHandler";
import SignUpByEmailHandler from "../domain/auth/handlers/SignUpByEmailHandler";
import UpdateUserInfoInAppSessionHandler from "../domain/auth/handlers/UpdateUserInfoInAppSessionHandler";

const TYPE_INFO: Record<string, string> = {
	
		[LoginByEmailHandler.name]: "LOGIN_BY_EMAIL_HANDLER",

		[LoginByTokenHandler.name]: "LOGIN_BY_TOKEN_HANDLER",

		[LogoutHandler.name]: "LOGOUT_HANDLER",

		[SignUpByEmailHandler.name]: "SIGN_UP_BY_EMAIL_HANDLER",

		[UpdateUserInfoInAppSessionHandler.name]: "UPDATE_USER_INFO_IN_APP_SESSION_HANDLER",
};

export default TYPE_INFO;
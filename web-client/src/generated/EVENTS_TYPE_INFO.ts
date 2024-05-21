import AppStarted from "../domain/auth/events/AppStarted";
import ChangedAppSessionState from "../domain/auth/events/ChangedAppSessionState";
import FailedAuthenticateByToken from "../domain/auth/events/FailedAuthenticateByToken";
import FailedLoginByEmail from "../domain/auth/events/FailedLoginByEmail";
import FailedLoginByToken from "../domain/auth/events/FailedLoginByToken";
import FailedSignUpByEmail from "../domain/auth/events/FailedSignUpByEmail";
import FailedUpdateUserInfoInAppSession from "../domain/auth/events/FailedUpdateUserInfoInAppSession";
import LoginByEmailFailed from "../domain/auth/events/LoginByEmailFailed";
import LoginDialogDidClose from "../domain/auth/events/LoginDialogDidClose";
import PersonalInfoDidChange from "../domain/auth/events/PersonalInfoDidChange";
import SignUpDialogDidClose from "../domain/auth/events/SignUpDialogDidClose";
import UpdatedUserPhoto from "../domain/auth/events/UpdatedUserPhoto";
import UserLoggedIn from "../domain/auth/events/UserLoggedIn";
import UserLoggedOut from "../domain/auth/events/UserLoggedOut";
import UserSignedUpByEmail from "../domain/auth/events/UserSignedUpByEmail";
import OpenedLoginDialog from "../presentation/events/OpenedLoginDialog";
import OpenedSignUpDialog from "../presentation/events/OpenedSignUpDialog";
import UserClosedFileDialogForUploadingUserPhoto from "../presentation/events/UserClosedFileDialogForUploadingUserPhoto";

const TYPE_INFO: Record<string, string> = {
	
		[AppStarted.name]: "APP_STARTED",

		[ChangedAppSessionState.name]: "CHANGED_APP_SESSION_STATE",

		[FailedAuthenticateByToken.name]: "FAILED_AUTHENTICATE_BY_TOKEN",

		[FailedLoginByEmail.name]: "FAILED_LOGIN_BY_EMAIL",

		[FailedLoginByToken.name]: "FAILED_LOGIN_BY_TOKEN",

		[FailedSignUpByEmail.name]: "FAILED_SIGN_UP_BY_EMAIL",

		[FailedUpdateUserInfoInAppSession.name]: "FAILED_UPDATE_USER_INFO_IN_APP_SESSION",

		[LoginByEmailFailed.name]: "LOGIN_BY_EMAIL_FAILED",

		[LoginDialogDidClose.name]: "LOGIN_DIALOG_DID_CLOSE",

		[PersonalInfoDidChange.name]: "PERSONAL_INFO_DID_CHANGE",

		[SignUpDialogDidClose.name]: "SIGN_UP_DIALOG_DID_CLOSE",

		[UpdatedUserPhoto.name]: "UPDATED_USER_PHOTO",

		[UserLoggedIn.name]: "USER_LOGGED_IN",

		[UserLoggedOut.name]: "USER_LOGGED_OUT",

		[UserSignedUpByEmail.name]: "USER_SIGNED_UP_BY_EMAIL",

		[OpenedLoginDialog.name]: "OPENED_LOGIN_DIALOG",

		[OpenedSignUpDialog.name]: "OPENED_SIGN_UP_DIALOG",

		[UserClosedFileDialogForUploadingUserPhoto.name]: "USER_CLOSED_FILE_DIALOG_FOR_UPLOADING_USER_PHOTO",
};

export default TYPE_INFO;
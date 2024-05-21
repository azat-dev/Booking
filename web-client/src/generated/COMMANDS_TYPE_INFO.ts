import LoginByEmail from "../domain/auth/commands/LoginByEmail";
import LoginByToken from "../domain/auth/commands/LoginByToken";
import Logout from "../domain/auth/commands/Logout";
import SignUpByEmail from "../domain/auth/commands/SignUpByEmail";
import UpdateUserInfoInAppSession from "../domain/auth/commands/UpdateUserInfoInAppSession";
import UploadNewUserPhoto from "../domain/auth/commands/UploadNewUserPhoto";
import CloseDialog from "../presentation/commands/CloseDialog";
import OpenFileDialogForUploadingUserPhoto from "../presentation/commands/OpenFileDialogForUploadingUserPhoto";
import OpenLoginDialog from "../presentation/commands/OpenLoginDialog";
import OpenSignUpDialog from "../presentation/commands/OpenSignUpDialog";
import OpenUserProfilePage from "../presentation/commands/OpenUserProfilePage";

const TYPE_INFO: Record<string, string> = {
	
		[LoginByEmail.name]: "LOGIN_BY_EMAIL",

		[LoginByToken.name]: "LOGIN_BY_TOKEN",

		[Logout.name]: "LOGOUT",

		[SignUpByEmail.name]: "SIGN_UP_BY_EMAIL",

		[UpdateUserInfoInAppSession.name]: "UPDATE_USER_INFO_IN_APP_SESSION",

		[UploadNewUserPhoto.name]: "UPLOAD_NEW_USER_PHOTO",

		[CloseDialog.name]: "CLOSE_DIALOG",

		[OpenFileDialogForUploadingUserPhoto.name]: "OPEN_FILE_DIALOG_FOR_UPLOADING_USER_PHOTO",

		[OpenLoginDialog.name]: "OPEN_LOGIN_DIALOG",

		[OpenSignUpDialog.name]: "OPEN_SIGN_UP_DIALOG",

		[OpenUserProfilePage.name]: "OPEN_USER_PROFILE_PAGE",
};

export default TYPE_INFO;
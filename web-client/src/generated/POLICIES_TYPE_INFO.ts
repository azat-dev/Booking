import WhenAppStartedThenTryLoginByToken from "../domain/auth/policies/WhenAppStartedThenTryLoginByToken";
import WhenFailedLoginByTokenThenUpdateAppSession from "../domain/auth/policies/WhenFailedLoginByTokenThenUpdateAppSession";
import WhenLoggedInThenUpdateAppSession from "../domain/auth/policies/WhenLoggedInThenUpdateAppSession";
import WhenLogoutThenUpdateAppSession from "../domain/auth/policies/WhenLogoutThenUpdateAppSession";
import WhenPersonalInfoChangedUpdateAppSession from "../domain/auth/policies/WhenPersonalInfoChangedUpdateAppSession";
import WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession from "../domain/auth/policies/WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession";
import WhenUserSignedUpThenUpdateAppSession from "../domain/auth/policies/WhenUserSignedUpThenUpdateAppSession";

const TYPE_INFO: Record<string, string> = {
	
		[WhenAppStartedThenTryLoginByToken.name]: "WHEN_APP_STARTED_THEN_TRY_LOGIN_BY_TOKEN",

		[WhenFailedLoginByTokenThenUpdateAppSession.name]: "WHEN_FAILED_LOGIN_BY_TOKEN_THEN_UPDATE_APP_SESSION",

		[WhenLoggedInThenUpdateAppSession.name]: "WHEN_LOGGED_IN_THEN_UPDATE_APP_SESSION",

		[WhenLogoutThenUpdateAppSession.name]: "WHEN_LOGOUT_THEN_UPDATE_APP_SESSION",

		[WhenPersonalInfoChangedUpdateAppSession.name]: "WHEN_PERSONAL_INFO_CHANGED_UPDATE_APP_SESSION",

		[WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession.name]: "WHEN_UPDATED_USER_PHOTO_THEN_UPDATE_USER_INFO_IN_APP_SESSION",

		[WhenUserSignedUpThenUpdateAppSession.name]: "WHEN_USER_SIGNED_UP_THEN_UPDATE_APP_SESSION",
};

export default TYPE_INFO;
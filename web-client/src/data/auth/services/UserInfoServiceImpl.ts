import PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService";
import PersonalUserInfo from "../../../domain/auth/values/PersonalUserInfo";
import Email from "../../../domain/auth/values/Email";
import UserId from "../../../domain/auth/values/UserId";
import FullName from "../../../domain/auth/values/FullName";
import FirstName from "../../../domain/auth/values/FirstName";
import LastName from "../../../domain/auth/values/LastName";
import PhotoPath from "../../../domain/auth/values/PhotoPath";
import {CommandsUpdateUserPhotoApi, QueriesCurrentUserApi} from "../../api/identity";
import uploadPhoto from "../../uploadPhoto.ts";

class UserInfoServiceImpl implements PersonalUserInfoService {

    public constructor(
        private queriesCurrentUserApi: QueriesCurrentUserApi,
        private commandsUpdateUserPhotoApi: CommandsUpdateUserPhotoApi
    ) {
    }

    public getUserInfo = async (): Promise<PersonalUserInfo> => {
        const userInfo = await this.queriesCurrentUserApi.getCurrentUser();
        const photo = userInfo.photo?.url ? new PhotoPath(userInfo.photo?.url) : null;

        return new PersonalUserInfo(
            UserId.fromString(userInfo.id),
            Email.dangerouslyCreate(userInfo.email),
            new FullName(
                FirstName.dangerouslyCreate(userInfo.fullName.firstName),
                LastName.dangerouslyCreate(userInfo.fullName.lastName)
            ),
            photo
        );
    };

    updateUserPhoto = async (id: UserId, photo: File): Promise<void> => {

        const response = await this.commandsUpdateUserPhotoApi.generateUploadUserPhotoUrl(
            {
                generateUploadUserPhotoUrlRequestBody: {
                    operationId: crypto.randomUUID(),
                    fileName: this.getFileNameWithoutExtension(photo),
                    fileExtension: this.getFileExtension(photo),
                    fileSize: photo.size
                }
            }
        );

        await uploadPhoto(response.objectPath.url, response.formData as any, photo);
        await this.commandsUpdateUserPhotoApi.updateUserPhoto({
            updateUserPhotoRequestBody: {
                operationId: crypto.randomUUID(),
                uploadedFile: response.objectPath
            }
        })
        console.log("response: ", response);
    }

    private getFileExtension = (file: File): string => {
        const parts = file.name.split(".");
        return parts[parts.length - 1];
    }

    private getFileNameWithoutExtension = (file: File): string => {
        const parts = file.name.split(".");
        parts.pop();
        return parts.join(".");
    }
}

export default UserInfoServiceImpl;
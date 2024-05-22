import PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService";
import {DefaultApi} from "../../API";
import PersonalUserInfo from "../../../domain/auth/values/PersonalUserInfo";
import Email from "../../../domain/auth/values/Email";
import UserId from "../../../domain/auth/values/UserId";
import FullName from "../../../domain/auth/values/FullName";
import FirstName from "../../../domain/auth/values/FirstName";
import LastName from "../../../domain/auth/values/LastName";
import PhotoPath from "../../../domain/auth/values/PhotoPath";

class UserInfoServiceImpl implements PersonalUserInfoService {

    public constructor(
        private api: DefaultApi
    ) {
    }

    public getUserInfo = async (): Promise<PersonalUserInfo> => {
        const userInfo = await this.api.apiWithAuthUsersCurrentGet();
        const photoUrl = userInfo.photo?.url ?? null;

        return new PersonalUserInfo(
            UserId.fromString(userInfo.id),
            Email.dangerouslyCreate(userInfo.email),
            new FullName(
                FirstName.dangerouslyCreate(userInfo.fullName.firstName),
                LastName.dangerouslyCreate(userInfo.fullName.lastName)
            ),
            photoUrl && new PhotoPath(photoUrl)
        );
    };

    updateUserPhoto = async (id: UserId, photo: File): Promise<void> => {

        const response = await this.api.generateUploadUserPhototUrl(
            {
                generateUploadUserPhotoUrlRequest: {
                    idempotentOperationId: "string",
                    fileName: this.getFileNameWithoutExtension(photo),
                    fileExtension: this.getFileExtension(photo),
                    fileSize: photo.size
                }
            }
        );

        await this.uploadPhoto(response.objectPath.url, response.formData as any, photo);
        await this.api.updateUserPhoto({
            updateUserPhotoRequest: {
                idempotentOperationId: "string",
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

    private uploadPhoto = async (url: string, params: Record<string, string>, photo: File): Promise<boolean> => {

        const formData = new FormData();

        for (const key in params) {
            formData.append(key, params[key]);
        }

        formData.append('Content-Type', photo.type);
        formData.append('file', photo);

        const result = await fetch(url, {
            method: "POST",
            body: formData
        });

        return result.status === 200 || result.status === 201 || result.status === 204;
    }

}

export default UserInfoServiceImpl;
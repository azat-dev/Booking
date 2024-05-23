import PersonalUserInfo from "../../../domain/auth/values/PersonalUserInfo";
import UserPhotoVM from "./user-photo/UserPhotoVM";
import {ReadonlySubject} from "../../utils/binding/Subject";
import Disposables from "../../utils/binding/Disposables";
import KeepType from "../../../domain/utils/KeepType.ts";
import mappedValue from "../../utils/binding/mappedValue.ts";

class PageUserProfileVM extends KeepType {

    public readonly photo: UserPhotoVM;
    private disposables = new Disposables();

    public constructor(
        userInfo: ReadonlySubject<PersonalUserInfo>,
        private uploadUserPhoto: () => Promise<void>
    ) {

        super();

        const fullName = mappedValue(userInfo, (v) => v.fullName);
        const photo = mappedValue(userInfo, v => v.photo);

        this.disposables.addItems(
            fullName,
            photo
        );

        this.photo = new UserPhotoVM(
            fullName,
            photo,
            this.uploadUserPhoto
        );
    }

    public dispose = () => {
        this.disposables.dispose();
    }
}

export default PageUserProfileVM;
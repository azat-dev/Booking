import KeepType from "../../../../domain/utils/KeepType.ts";

class ProfileButtonAnonymousVM extends KeepType {

    public constructor(
        private readonly onLogin: () => void,
        private readonly onSignUp: () => void
    ) {
        super();
    }

    public login = () => {
        this.onLogin();
    };

    public signUp = () => {
        this.onSignUp();
    };
}

export default ProfileButtonAnonymousVM;

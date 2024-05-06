import SignUpDialogViewModel from "../dialogs/sign-up-dialog/SignUpDialogViewModel";

export default interface SignUpDialogVMFactory {
    make(): SignUpDialogViewModel;
}

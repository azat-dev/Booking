import React from "react";

import PropsSignUpDialog from "./props";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import {
    Modal,
    ModalDialog,
    DialogTitle,
    Stack,
    FormControl,
    FormLabel,
    Input,
    Button,
    ModalClose,
    Link,
    Typography,
} from "@mui/joy";

import style from "./style.module.scss";
import { desktop, mobile, tablet } from "../../utils/selectors";
import useScreenType, { ScreenType } from "../../utils/hooks/useScreenType";
import FormInput from "./form-input/FormInput";

const SignUpDialog = ({ vm }: PropsSignUpDialog) => {
    const [isProcessing, showWrongCredentialsError] = useUpdatesFrom(
        vm.isProcessing,
        vm.showWrongCredentialsError
    );

    const screenType = useScreenType();

    return (
        <Modal open={true} onClose={vm.close}>
            <ModalDialog
                size="lg"
                layout={
                    screenType === ScreenType.DESKTOP ? "center" : "fullscreen"
                }
                sx={(theme) => {
                    return {
                        [mobile(theme)]: {},
                        [tablet(theme)]: {},
                        [desktop(theme)]: {
                            minWidth: 500,
                        },
                    };
                }}
            >
                <ModalClose />
                <DialogTitle>Welcome!</DialogTitle>
                <Typography level="h3">Create a free account</Typography>
                <form
                    onSubmit={(event: React.FormEvent<HTMLFormElement>) => {
                        event.preventDefault();
                        vm.submit();
                    }}
                >
                    <Stack spacing={0}>
                        <FormInput
                            vm={vm.firstNameInput}
                            label="First Name"
                            type="text"
                            placeholder="First Name"
                        />
                        <FormInput
                            vm={vm.lastNameInput}
                            label="Last Name"
                            type="text"
                            placeholder="Last Name"
                        />
                        <FormInput
                            vm={vm.emailInput}
                            label="Email"
                            type="email"
                            placeholder="johndoe@email.com"
                        />

                        <FormInput
                            vm={vm.passwordInput}
                            label="Password"
                            type="password"
                            placeholder="Password"
                        />

                        <Typography
                            color="danger"
                            sx={{ opacity: showWrongCredentialsError ? 1 : 0 }}
                        >
                            Wrong credentials
                        </Typography>
                        <Button
                            type="submit"
                            size="lg"
                            loading={isProcessing}
                            loadingPosition="start"
                        >
                            Create Account
                        </Button>
                        <Typography
                            endDecorator={
                                <Link onClick={vm.logIn}>Log in</Link>
                            }
                            fontSize="sm"
                            sx={{ alignSelf: "center" }}
                        >
                            Have an account?
                        </Typography>
                    </Stack>
                </form>
            </ModalDialog>
        </Modal>
    );
};

export default React.memo(SignUpDialog);

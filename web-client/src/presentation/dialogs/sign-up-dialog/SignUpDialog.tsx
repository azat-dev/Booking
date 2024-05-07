import React from "react";

import PropsSignUpDialog from "./props";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import {Alert, Button, DialogTitle, Link, Modal, ModalClose, ModalDialog, Stack, Typography,} from "@mui/joy";
import {desktop, mobile, tablet} from "../../utils/selectors";
import useScreenType, {ScreenType} from "../../utils/hooks/useScreenType";
import FormInput from "./form-input/FormInput";
import {InfoOutlined} from "@mui/icons-material";

const SignUpDialog = ({vm}: PropsSignUpDialog) => {
    const [isProcessing, errorText] = useUpdatesFrom(
        vm.isProcessing,
        vm.errorText
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
                <ModalClose/>
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

                        {!!errorText &&
                            <Alert
                                variant="outlined"
                                color="danger"
                                startDecorator={<InfoOutlined/>}
                                sx={{opacity: !!errorText ? 1 : 0}}
                            >
                                {errorText}
                            </Alert>
                        }
                        <br/>

                        <Button
                            type="submit"
                            size="lg"
                            loading={isProcessing}
                            loadingPosition="start"
                        >
                            Create Account
                        </Button>
                        <br/>
                        <Typography
                            endDecorator={
                                <Link onClick={vm.logIn}>Log in</Link>
                            }
                            fontSize="sm"
                            sx={{alignSelf: "center"}}
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

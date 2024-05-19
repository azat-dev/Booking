import React from "react";

import PropsLoginDialog from "./props";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import {Alert, Button, DialogTitle, Link, Modal, ModalClose, ModalDialog, Stack, Typography,} from "@mui/joy";
import FormInput from "../sign-up-dialog/form-input/FormInput";
import {InfoOutlined} from "@mui/icons-material";

const LoginDialog = ({vm}: PropsLoginDialog) => {
    const [isProcessing, showWrongCredentialsError] = useUpdatesFrom(
        vm.isProcessing,
        vm.showWrongCredentialsError
    );

    return (
        <Modal open={true} onClose={vm.close}>
            <ModalDialog size="lg">
                <ModalClose/>
                <DialogTitle>Welcome!</DialogTitle>
                <Typography level="body-sm">Sign in to continue.</Typography>
                <form
                    onSubmit={(event: React.FormEvent<HTMLFormElement>) => {
                        event.preventDefault();
                        vm.submit();
                    }}
                >
                    <Stack spacing={1}>
                        <Stack spacing={0.3}>
                            <FormInput
                                label="Email"
                                placeholder="johndoe@email.com"
                                type="email"
                                vm={vm.emailInput}
                            />
                            <FormInput
                                label="Password"
                                placeholder="Password..."
                                type="password"
                                vm={vm.passwordInput}
                            />
                        </Stack>

                        <Stack spacing={2}>
                            {showWrongCredentialsError &&
                                <Alert
                                    variant="outlined"
                                    color="danger"
                                    startDecorator={<InfoOutlined/>}
                                >
                                    Wrong email or password
                                </Alert>
                            }
                            <Button
                                type="submit"
                                size="lg"
                                loading={isProcessing}
                                loadingPosition="start"
                            >
                                Log In
                            </Button>
                        </Stack>
                        <Typography
                            endDecorator={
                                <a onClick={vm.signUp}>Sign up</a>
                            }
                            fontSize="sm"
                            sx={{alignSelf: "center"}}
                        >
                            Don&apos;t have an account?
                        </Typography>
                    </Stack>
                </form>
            </ModalDialog>
        </Modal>
    );
};

export default React.memo(LoginDialog);

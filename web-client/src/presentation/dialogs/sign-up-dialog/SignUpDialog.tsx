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

const SignUpDialog = ({ vm }: PropsSignUpDialog) => {
    const [isProcessing, showWrongCredentialsError] = useUpdatesFrom(
        vm.isProcessing,
        vm.showWrongCredentialsError
    );

    return (
        <Modal open={true} onClose={vm.close}>
            <ModalDialog size="lg">
                <ModalClose />
                <DialogTitle>Welcome!</DialogTitle>
                <Typography level="body-sm">
                    Start your jorney now! <br />
                    Create a free account
                </Typography>
                <form
                    onSubmit={(event: React.FormEvent<HTMLFormElement>) => {
                        event.preventDefault();
                        vm.submit();
                    }}
                >
                    <Stack spacing={2}>
                        <FormControl disabled={isProcessing}>
                            <FormLabel>Email</FormLabel>
                            <Input
                                autoFocus
                                required
                                type="email"
                                name="email"
                                placeholder="johndoe@email.com"
                            />
                        </FormControl>
                        <FormControl disabled={isProcessing}>
                            <FormLabel>Password</FormLabel>
                            <Input
                                required
                                type="password"
                                placeholder="password"
                            />
                        </FormControl>
                        <FormControl disabled={isProcessing}>
                            <FormLabel>Password Confirmation</FormLabel>
                            <Input
                                required
                                type="password"
                                placeholder="password confirmation"
                            />
                        </FormControl>
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

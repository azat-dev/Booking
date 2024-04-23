import React, { useEffect, useLayoutEffect } from "react";

import PropsAuthDialog from "./props";
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
    Chip,
    Divider,
    Typography,
} from "@mui/joy";

import style from "./style.module.scss";

const AuthDialog = ({ vm }: PropsAuthDialog) => {
    const [isProcessing, showWrongCredentialsError] = useUpdatesFrom(
        vm.isProcessing,
        vm.showWrongCredentialsError
    );

    return (
        <Modal open={true} onClose={vm.close}>
            <ModalDialog size="lg">
                <ModalClose />
                <DialogTitle>Welcome!</DialogTitle>
                <Typography level="body-sm">Sign in to continue.</Typography>
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
                            Log In
                        </Button>
                        <Typography
                            endDecorator={<Link href="/sign-up">Sign up</Link>}
                            fontSize="sm"
                            sx={{ alignSelf: "center" }}
                        >
                            Don&apos;t have an account?
                        </Typography>
                    </Stack>
                </form>
            </ModalDialog>
        </Modal>
    );
};

export default React.memo(AuthDialog);

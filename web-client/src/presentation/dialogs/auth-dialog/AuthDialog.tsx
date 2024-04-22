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
    const [isOpen, isProcessing, showWrongCredentialsError] = useUpdatesFrom(
        vm.isOpen,
        vm.isProcessing,
        vm.showWrongCredentialsError
    );

    useLayoutEffect(() => {
        if (!isOpen) {
            vm.didClose();
            return;
        }
    }, [isOpen]);

    useLayoutEffect(() => {
        vm.didMount();
    }, []);

    return (
        <Modal open={isOpen} onClose={vm.close}>
            <ModalDialog size="lg">
                <ModalClose />
                <DialogTitle>Log In</DialogTitle>
                <form
                    onSubmit={(event: React.FormEvent<HTMLFormElement>) => {
                        event.preventDefault();
                        vm.submit();
                    }}
                >
                    <Stack spacing={2}>
                        <FormControl disabled={isProcessing}>
                            <FormLabel>Email</FormLabel>
                            <Input autoFocus required type="email" />
                        </FormControl>
                        <FormControl disabled={isProcessing}>
                            <FormLabel>Password</FormLabel>
                            <Input required type="password" />
                        </FormControl>
                        {showWrongCredentialsError && (
                            <Typography color="danger">
                                Wrong credentials
                            </Typography>
                        )}
                        <Button
                            type="submit"
                            size="lg"
                            loading={isProcessing}
                            loadingPosition="start"
                        >
                            Log In
                        </Button>
                        <Divider>
                            <Chip variant="soft" color="neutral" size="sm">
                                OR
                            </Chip>
                        </Divider>
                        <Button type="submit" size="md" color="neutral">
                            Sign Up
                        </Button>
                    </Stack>
                </form>
            </ModalDialog>
        </Modal>
    );
};

export default React.memo(AuthDialog);

import React from "react";

import PropsProfileButtonAnonymous from "./props.ts";
import Stack from "@mui/joy/Stack";
import Button from "@mui/joy/Button";

const ProfileButtonAnonymous = ({vm}: PropsProfileButtonAnonymous) => {
    return (
        <Stack direction="row" spacing={1}>
            <Button variant="plain" onClick={vm.login}>Log in</Button>
            <Button onClick={vm.signUp}>Sign up</Button>
        </Stack>
    );
};

export default React.memo(ProfileButtonAnonymous);

import { Theme } from "@mui/joy";

export const mobile = (theme: Theme) => {
    return theme.breakpoints.down("sm");
};

export const tablet = (theme: Theme) => {
    return theme.breakpoints.up("sm");
};

export const desktop = (theme: Theme) => {
    return theme.breakpoints.up("md");
};

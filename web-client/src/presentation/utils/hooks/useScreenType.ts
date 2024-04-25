import { useMediaQuery } from "usehooks-ts";

export enum ScreenType {
    MOBILE,
    DESKTOP,
    TABLET,
}

const useScreenType = (): ScreenType => {
    // const isSm = useMediaQuery("(max-width: 600px)");
    const isBetweenSmAndMd = useMediaQuery("(min-width: 600px)");
    const isMd = useMediaQuery("(min-width: 900px)");

    if (isMd) {
        return ScreenType.DESKTOP;
    }

    if (isBetweenSmAndMd) {
        return ScreenType.TABLET;
    }

    return ScreenType.MOBILE;
};

export default useScreenType;

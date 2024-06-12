import ListingsDataConfig from "../presentation/app/config/data/ListingsDataConfig.ts";
import ListingsCommands from "../presentation/app/config/domain/ListingsCommands.ts";
import HostingPagesVmConfig from "../presentation/app/config/presentation/hosting/HostingPagesVmConfig.ts";
import LocalAuthDataConfig from "../presentation/app/config/data/LocalAuthDataConfig.ts";
import DomainConfig from "../presentation/app/config/domain/DomainConfig.ts";
import HostingComponentsConfig from "../presentation/app/config/presentation/hosting/HostingComponentsConfig.ts";
import AppSession from "../domain/auth/entities/AppSession.ts";
import HostingPagesViewConfig from "../presentation/app/config/presentation/hosting/HostingPagesViewConfig.tsx";
import HostingCommands from "../presentation/app/config/presentation/hosting/HostingCommands.ts";
import CommonDialogsCommands from "../presentation/app/config/presentation/common/CommonDialogsCommands.ts";
import identityCommands from "../presentation/app/config/domain/IdentityCommands.ts";
import AppSessionAuthenticated from "../domain/auth/entities/AppSessionAuthenticated.ts";

class HostingModule {

    private constructor() {

    }

    public static make = async (
        baseApiUrl: string,
        appSession: AppSessionAuthenticated,
        localAuthDataConfig: LocalAuthDataConfig,
        identityCommands: identityCommands,
        commonDialogsCommands: CommonDialogsCommands,
        domainConfig: DomainConfig,
    ) => {

        const bus = domainConfig.bus;

        const listingsDataConfig = new ListingsDataConfig(
            baseApiUrl,
            localAuthDataConfig.localAuthDataRepository()
        );

        const listingsCommands = new ListingsCommands(
            listingsDataConfig.listingsPrivateQueriesApi(),
            listingsDataConfig.listingsModificationsApi(),
            listingsDataConfig.commandsListingsPhotoApi(),
            bus
        );

        const components = new HostingComponentsConfig(
            commonDialogsCommands,
            identityCommands,
            appSession,
        );

        const hostingCommands = new HostingCommands(listingsCommands, bus);

        const vmConfig = new HostingPagesVmConfig(
            appSession,
            components,
            hostingCommands,
            listingsCommands
        );

        return new HostingPagesViewConfig(vmConfig);
    };
}

export default HostingModule;
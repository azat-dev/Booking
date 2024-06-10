import ListingsDataConfig from "../presentation/app/config/data/ListingsDataConfig.ts";
import ListingsCommandHandlers from "../presentation/app/config/domain/ListingsCommandHandlers.ts";
import HostingPagesVmConfig from "../presentation/app/config/presentation/hosting/HostingPagesVmConfig.ts";
import LocalAuthDataConfig from "../presentation/app/config/data/LocalAuthDataConfig.ts";
import DomainConfig from "../presentation/app/config/domain/DomainConfig.ts";
import HostingComponentsConfig from "../presentation/app/config/presentation/hosting/HostingComponentsConfig.ts";
import AppSession from "../domain/auth/entities/AppSession.ts";
import HostingPagesViewConfig from "../presentation/app/config/presentation/hosting/HostingPagesViewConfig.tsx";
import HostingCommandsHandlers
    from "../presentation/app/config/presentation/hosting/HostingCommandsHandlers.ts";

class HostingModule {

    private constructor() {

    }

    public static make = async (
        baseApiUrl: string,
        appSession: AppSession,
        localAuthDataConfig: LocalAuthDataConfig,
        domainConfig: DomainConfig,
    ) => {

        const bus = domainConfig.bus;

        const listingsDataConfig = new ListingsDataConfig(
            baseApiUrl,
            localAuthDataConfig.localAuthDataRepository()
        );

        domainConfig.addCommandHandlersProvider(
            new ListingsCommandHandlers(
                listingsDataConfig.listingsPrivateQueriesApi(),
                listingsDataConfig.listingsModificationsApi(),
                listingsDataConfig.commandsListingsPhotoApi(),
                bus
            )
        );

        domainConfig.addCommandHandlersProvider(
            new HostingCommandsHandlers(
                bus
            )
        );

        const components = new HostingComponentsConfig(appSession, bus);

        const vmConfig = new HostingPagesVmConfig(
            components,
            bus
        );
        return new HostingPagesViewConfig(vmConfig);
    };
}

export default HostingModule;
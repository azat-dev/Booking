import ListingsDataConfig from "../presentation/app/config/data/ListingsDataConfig.ts";
import ListingsCommandHandlersProvider from "../presentation/app/config/domain/ListingsCommandHandlersProvider.ts";
import HostingPagesConfig from "../presentation/app/config/presentation/hosting/HostingPagesConfig.ts";
import LocalAuthDataConfig from "../presentation/app/config/data/LocalAuthDataConfig.ts";
import DomainConfig from "../presentation/app/config/domain/DomainConfig.ts";
import HostingComponentsConfig from "../presentation/app/config/presentation/hosting/HostingComponentsConfig.ts";
import AppSession from "../domain/auth/entities/AppSession.ts";

class HostingsModule {

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

        const listingsCommandHandlers = new ListingsCommandHandlersProvider(
            listingsDataConfig.listingsPrivateQueriesApi(),
            listingsDataConfig.listingsModificationsApi(),
            bus
        );

        domainConfig.addCommandHandlersProvider(listingsCommandHandlers);

        const components = new HostingComponentsConfig(appSession, bus);

        return new HostingPagesConfig(
            components,
            bus
        );
    };
}

export default HostingsModule;
import Bus from "../../../../domain/utils/Bus.ts";
import PoliciesProvider from "./PoliciesProvider.ts";

class DomainConfig {

    private policiesProviders: PoliciesProvider[];

    public constructor(public readonly bus: Bus) {
        this.policiesProviders = [];

        this.bus.subscribe(async event => {
            this.policiesProviders.forEach(provider => {

                const policies = provider.getForEvent(event);
                if (!policies) {
                    return;
                }

                policies.forEach(policy => {
                    console.log("%cDOMAIN/POLICY: ", "color: #FF33FF; font-weight: bold;", policy.type);
                    policy.execute(event)
                });
            });
        });
    }

    public registerPolicies = (provider: PoliciesProvider): void => {
        this.policiesProviders.push(provider);
    }
}

export default DomainConfig;
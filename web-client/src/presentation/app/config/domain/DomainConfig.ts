import Bus from "../../../../domain/utils/Bus.ts";
import CommandHandlersProvider from "./CommandHandlersProvider.ts";
import PoliciesProvider from "./PoliciesProvider.ts";
import Command from "../../../../domain/utils/Command.ts";

class DomainConfig {

    private policiesProviders: PoliciesProvider[];

    private commandHandlersProviders: CommandHandlersProvider[]

    public constructor(public readonly bus: Bus) {

        this.commandHandlersProviders = [];
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

            if (!(event instanceof Command)) {
                return;
            }

            const command = event;

            this.commandHandlersProviders.forEach(provider => {

                const handler = provider.getHandlerForCommand(command);
                if (!handler) {
                    return;
                }

                console.log(`%cDOMAIN/HANDLER: %c${handler.type}`, 'color: #FF33FF; font-weight: bold;', command);
                handler.execute(command);
            })
        });
    }

    public addPoliciesProvider = (provider: PoliciesProvider): void => {
        this.policiesProviders.push(provider);
    }

    public addCommandHandlersProvider = (provider: CommandHandlersProvider): void => {
        this.commandHandlersProviders.push(provider);
    }
}

export default DomainConfig;
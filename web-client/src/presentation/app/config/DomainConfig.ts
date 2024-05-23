import DomainPoliciesConfig from "./DomainPoliciesConfig";
import Bus from "../../../domain/utils/Bus";
import DomainCommandHandlersConfig from "./DomainCommandHandlersConfig";

class DomainConfig {

    public constructor(
        private readonly policies: DomainPoliciesConfig,
        private readonly commandHandlers: DomainCommandHandlersConfig,
        public readonly bus: Bus,
    ) {
        this.registerPolicies();
        this.registerCommandHandlers();
    }

    private registerPolicies = (): void => {


        this.bus.subscribe(async event => {

            const policies = this.policies.getForEvent(event);
            if (!policies) {
                return;
            }

            policies.forEach(policy => {
                console.log("%cDOMAIN/POLICY: ", "color: #FF33FF; font-weight: bold;", policy.type);
                policy.execute(event)
            });
        });
    }

    private registerCommandHandlers = (): void => {

        this.bus.subscribe(async command => {

            const handler = this.commandHandlers.getHandlerForCommand(command);
            if (!handler) {
                return;
            }

            console.log(`%cDOMAIN/HANDLER: %c${handler.type}`, 'color: #FF33FF; font-weight: bold;', command);
            handler.execute(command);
        });
    }
}

export default DomainConfig;
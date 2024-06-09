import AppEvent from "../../../../domain/utils/AppEvent.ts";
import Policy from "../../../../domain/utils/Policy.ts";

export default interface PoliciesProvider {

    getForEvent(event: AppEvent): Policy[];
}
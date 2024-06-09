import Command from "../../utils/Command";

class CreateDraftListing extends Command {
    public constructor(
        public readonly title: string
    ) {
        super();
    }
}

export default CreateDraftListing;
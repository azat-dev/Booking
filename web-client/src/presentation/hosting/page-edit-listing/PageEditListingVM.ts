import TitleEditorVM from "./title-editor/TitleEditorVM.ts";
import Subject from "../../utils/binding/Subject.ts";
import value from "../../utils/binding/value.ts";
import DescriptionEditorVM from "./description-editor/DescriptionEditorVM.ts";
import PhotosEditorVM from "./photos-editor/PhotosEditorVM.ts";
import LoadingButtonVM from "../../components/LoadingButtonVM.ts";
import ListingId from "../../../domain/listings/values/ListingId.ts";
import VM from "../../utils/VM.ts";
import {ListingPhotoPath, ListingPrivateDetails} from "../../../data/api/listings";

class PageEditListingVM extends VM {

    public content!: Subject<Content>;
    public steps!: Subject<boolean[]>;
    public nextButton: LoadingButtonVM;
    public backButton: LoadingButtonVM;
    private step!: Step;

    public constructor(
        listingId: ListingId | null,
        initialStep: 'title' | 'description' | 'photo' | null,
        private readonly createDraft: (title: string) => Promise<ListingId>,
        private readonly addNewPhoto: (id: ListingId) => Promise<void>,
        private readonly loadListingDetails: (id: ListingId) => Promise<ListingPrivateDetails>,
        private readonly updateTitle: (id: ListingId, title: string) => Promise<void>,
        private readonly updateDescription: (id: ListingId, description: string) => Promise<void>,
        private readonly loadPhotos: (id: ListingId) => Promise<ListingPhotoPath[]>,
        private readonly updateParams: (step: string | null, listingId: string | null) => void
    ) {

        super();

        this.nextButton = new LoadingButtonVM(
            false,
            false,
            this.next
        );

        this.backButton = new LoadingButtonVM(
            false,
            false,
            this.back
        );

        if (!initialStep || !listingId) {
            this.moveToTitle(listingId);
            return;
        }

        switch (initialStep) {
            case 'title':
                this.moveToTitle(listingId);
                break;
            case 'description':
                this.moveToDescription(listingId);
                break;
            case 'photo':
                this.moveToPhotos(listingId);
                break;
        }
    }

    public load = async (listingId: ListingId) => {
        this.enableButtons();
    }

    public displayFinishedProcessingNextStep = () => {
        this.enableButtons();
    }

    public moveToTitle = (listingId: ListingId | null) => {

        const step = new TitleStepVM(
            listingId,
            this.moveToDescription,
            this.displayProcessingNextStep,
            this.displayFinishedProcessingNextStep,
            async (title) => {
                const listingId = await this.createDraft(title);
                this.updateParams(null, listingId.val);
                return listingId;
            },
            this.updateTitle,
            async (listingId: ListingId) => {

                if (!listingId) {
                    return '';
                }

                const details = await this.loadListingDetails(listingId);
                return details.title;
            }
        );

        this.setStep(step);
    }

    public next = async () => {
        await this.step.next();
    }

    public back = async () => {

    }

    public enableButtons = () => {
        this.enableNextButton();
        this.enableBackButton();

    }

    private moveToDescription = (listingId: ListingId) => {
        this.updateParams('description', listingId.val);
        this.setStep(
            new DescriptionStepVM(
                listingId,
                this.moveToPhotos,
                this.displayProcessingNextStep,
                this.displayFinishedProcessingNextStep,
                this.updateDescription,
                async (listingId: ListingId) => {
                    const details = await this.loadListingDetails(listingId);
                    return details.description ?? '';
                }
            )
        );
    }

    private moveToPhotos = async (listingId: ListingId) => {
        this.updateParams('photo', listingId.val);
        this.setStep(
            new PhotosStepVM(
                listingId,
                async (listingId) => {
                    alert('done');
                },
                this.displayProcessingNextStep,
                this.displayFinishedProcessingNextStep,
                this.addNewPhoto,
                this.loadPhotos
            )
        );
    }

    private setStep = (step: Step) => {

        if (this.step) {
            this.step.editor.canMoveNext.dispose();
        }

        this.step = step;
        const newEditor = this.step.editor;

        if (!this.content) {
            this.content = value(newEditor);
        } else {
            this.content.set(newEditor);
        }

        this.updateSteps();

        this.nextButton.isDisabled.set(!newEditor.canMoveNext.value);

        this.cleanOnDestroy(
            this.step.editor.canMoveNext.listen((canMoveNext) => {
                this.nextButton.isDisabled.set(!canMoveNext);
            })
        );
    }

    private getCurrentStepIndex = (): number => {

        if (this.step instanceof TitleStepVM) {
            return 0;
        }

        if (this.step instanceof DescriptionStepVM) {
            return 1;
        }

        if (this.step instanceof PhotosStepVM) {
            return 2;
        }

        return 0;
    }

    private updateSteps = () => {

        const currentStepIndex = this.getCurrentStepIndex();
        if (this.steps) {
            this.steps.set(getSteps(currentStepIndex, 4));
            return;
        }

        this.steps = value(getSteps(currentStepIndex, 4));
    }

    private displayProcessingNextStep = () => {
        this.nextButton.updateIsDisabled(true);
        this.nextButton.updateIsLoading(true);

        this.backButton.updateIsDisabled(true);
    }

    private enableNextButton = () => {
        this.nextButton.updateIsDisabled(false);
        this.nextButton.updateIsLoading(false);
    }

    private enableBackButton = () => {
        this.backButton.updateIsDisabled(false);
        this.backButton.updateIsLoading(false);
    }
}

export type Content = TitleEditorVM | DescriptionEditorVM | PhotosEditorVM;

const getSteps = (activeIndex: number, numberOfSteps: number): boolean[] => {

    const steps = [];

    for (let i = 0; i < numberOfSteps + 1; i++) {
        steps.push(i < activeIndex);
    }

    return steps;
}

type Step = TitleStepVM | DescriptionStepVM | PhotosStepVM;

class TitleStepVM {

    public editor: TitleEditorVM;

    public constructor(
        private readonly listingId: ListingId | null,
        private readonly moveToNextStep: (listingId: ListingId) => void,
        private readonly displayProcessingNextStep: () => void,
        private readonly displayFinishedProcessingNextStep: () => void,
        private readonly createDraft: (title: string) => Promise<ListingId>,
        private readonly updateTitle: (listingId: ListingId, title: string) => Promise<void>,
        getInitialListingTitle: (listingId: ListingId) => Promise<string>
    ) {
        this.editor = new TitleEditorVM(
            async (): Promise<string> => {
                if (!listingId) {
                    return '';
                }
                return await getInitialListingTitle(listingId);
            }
        );
    }

    public next = async () => {

        const newTitle = this.editor.title.value;
        this.displayProcessingNextStep();

        try {

            if (this.listingId) {
                await this.updateTitle(this.listingId, newTitle);
                this.displayFinishedProcessingNextStep();
                this.moveToNextStep(this.listingId);
                return;
            }

            const listingId = await this.createDraft(newTitle);
            this.displayFinishedProcessingNextStep();
            this.moveToNextStep(listingId);

        } catch (e) {
            this.displayFinishedProcessingNextStep();
        }
    }
}

class DescriptionStepVM {

    public editor: DescriptionEditorVM;

    public constructor(
        private readonly listingId: ListingId,
        private readonly moveToNextStep: (listingId: ListingId) => Promise<void>,
        private readonly displayProcessingNextStep: () => void,
        private readonly displayFinishedProcessingNextStep: () => void,
        private readonly updateDescription: (id: ListingId, description: string) => Promise<void>,
        getListingDescription: (listingId: ListingId) => Promise<string>
    ) {
        this.editor = new DescriptionEditorVM(
            async () => {
                if (!listingId) {
                    return '';
                }

                return getListingDescription(listingId);
            }
        );
    }

    public next = async () => {

        this.displayProcessingNextStep();

        try {
            await this.updateDescription(this.listingId, this.editor.description.value);

            this.displayFinishedProcessingNextStep();
            this.moveToNextStep(this.listingId);
        } catch (e) {
            this.displayFinishedProcessingNextStep();
        }
    }
}

class PhotosStepVM {

    public editor: PhotosEditorVM;

    public constructor(
        private listingId: ListingId,
        private readonly moveToNextStep: (listingId: ListingId) => void,
        private readonly displayProcessingNextStep: () => void,
        private readonly displayFinishedProcessingNextStep: () => void,
        addNewPhoto: (listingId: ListingId) => Promise<void>,
        loadPhotos: (listingId: ListingId) => Promise<ListingPhotoPath[]>
    ) {
        this.editor = new PhotosEditorVM(
            listingId,
            addNewPhoto,
            loadPhotos
        );
    }

    public next = async () => {
        this.displayFinishedProcessingNextStep();
        this.moveToNextStep(this.listingId);
    }
}

export default PageEditListingVM;
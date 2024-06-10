import TitleEditorVM from "./title-editor/TitleEditorVM.ts";
import Subject from "../../utils/binding/Subject.ts";
import value from "../../utils/binding/value.ts";
import DescriptionEditorVM from "./description-editor/DescriptionEditorVM.ts";
import PhotosEditorVM from "./photos-editor/PhotosEditorVM.ts";
import LoadingButtonVM from "../../components/LoadingButtonVM.ts";
import ListingId from "../../../domain/listings/values/ListingId.ts";
import VM from "../../utils/VM.ts";

class PageEditListingVM extends VM {

    public content!: Subject<Content>;
    public steps!: Subject<boolean[]>;
    public nextButton: LoadingButtonVM;
    public backButton: LoadingButtonVM;
    public delegate!: {
        createDraft: (title: string) => void;
        updateDescription: (id: ListingId, description: string) => void;
        addNewPhoto: (id: ListingId) => void;
        loadPhotos: (id: ListingId) => void;
    };
    private step!: Step;

    public constructor(
        private readonly initialListingId: ListingId | null
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

        this.moveToTitle();
    }

    public displayFinishedProcessingNextStep = () => {
        this.enableButtons();
    }

    public moveToTitle = () => {

        const step = new TitleStepVM(
            '',
            this.moveToDescription,
            this.displayProcessingNextStep,
            this.displayFinishedProcessingNextStep,
            (title) => {
                this.delegate.createDraft(title);
            },
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

    public displayFailedCreateDraft = () => {
        if (this.step instanceof TitleStepVM) {
            this.step.displayFailedCreateDraft();
        }
    }

    public displayFailedUpdateDraft = () => {

        if (this.step instanceof DescriptionStepVM) {
            this.step.displayFailedUpdateDraft();
        }
    }

    public displayCreatedDraft = (listingId: ListingId) => {
        if (this.step instanceof TitleStepVM) {
            this.step.displayCreatedDraft(listingId);
        }
    }

    public displayUpdatedDescription = () => {
        if (this.step instanceof DescriptionStepVM) {
            this.step.displayUpdatedDescription();
        }
    }

    public displayAddedNewPhoto = () => {

        if (this.step.editor instanceof PhotosEditorVM) {
            this.step.editor.displayAddedNewPhoto();
        }
    }

    public displayFailedFailedToAddPhoto = () => {
        if (this.step.editor instanceof PhotosEditorVM) {
            this.step.editor.displayFailedToAddNewPhoto();
        }
    }

    private moveToDescription = (listingId: ListingId) => {
        this.setStep(
            new DescriptionStepVM(
                listingId,
                this.moveToPhotos,
                this.displayProcessingNextStep,
                this.displayFinishedProcessingNextStep,
                this.delegate.updateDescription
            )
        );
    }

    private moveToPhotos = async (listingId: ListingId) => {

        this.setStep(
            new PhotosStepVM(
                listingId,
                async (listingId) => {
                    alert('done');
                },
                this.displayProcessingNextStep,
                this.displayFinishedProcessingNextStep,
                this.delegate.addNewPhoto,
                this.delegate.loadPhotos
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

    public displayLoadedListingDetails = (data: any) => {

        if (this.step.editor instanceof PhotosEditorVM) {
            this.step.editor.displayPhotos(data.photos);
        }
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
        initialValue: string,
        private readonly moveToNextStep: (listingId: ListingId) => void,
        private readonly displayProcessingNextStep: () => void,
        private readonly displayFinishedProcessingNextStep: () => void,
        private readonly createdDraft: (title: string) => void
    ) {
        this.editor = new TitleEditorVM(initialValue);
    }

    public next = async () => {

        this.displayProcessingNextStep();
        this.createdDraft(this.editor.title.value);
    }

    public displayFailedCreateDraft = () => {
        this.displayFinishedProcessingNextStep();
    }

    public displayCreatedDraft = (listingId: ListingId) => {
        this.displayFinishedProcessingNextStep();
        this.moveToNextStep(listingId);
    }
}

class DescriptionStepVM {

    public editor: DescriptionEditorVM;

    public constructor(
        private readonly listingId: ListingId,
        private readonly moveToNextStep: (listingId: ListingId) => Promise<void>,
        private readonly displayProcessingNextStep: () => void,
        private readonly displayFinishedProcessingNextStep: () => void,
        private readonly updateDescription: (id: ListingId, description: string) => void
    ) {
        this.editor = new DescriptionEditorVM('initial value');
    }

    public next = async () => {

        this.displayProcessingNextStep();
        this.updateDescription(this.listingId, this.editor.description.value);
    }

    public displayFailedUpdateDraft = () => {
        this.displayFinishedProcessingNextStep();
    }

    public displayUpdatedDescription = () => {
        this.displayFinishedProcessingNextStep();
        this.moveToNextStep(this.listingId);
    }
}

class PhotosStepVM {

    public editor: PhotosEditorVM;

    public constructor(
        private listingId: ListingId,
        private readonly moveToNextStep: (listingId: ListingId) => void,
        private readonly displayProcessingNextStep: () => void,
        private readonly displayFinishedProcessingNextStep: () => void,
        addNewPhoto: (listingId: ListingId) => void,
        loadPhotos: (listingId: ListingId) => void
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
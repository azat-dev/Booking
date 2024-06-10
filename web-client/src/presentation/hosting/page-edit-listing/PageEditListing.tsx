import React from "react";
import PropsPageEditListing from "./props";

import style from './style.module.scss';
import Button from "@mui/joy/Button";
import Box from "@mui/joy/Box";
import {Step, stepClasses, Stepper} from "@mui/joy";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom.ts";
import TitleEditorVM from "./title-editor/TitleEditorVM.ts";
import TitleEditor from "./title-editor/TitleEditor.tsx";
import DescriptionEditor from "./description-editor/DescriptionEditor.tsx";
import DescriptionEditorVM from "./description-editor/DescriptionEditorVM.ts";
import PhotosEditorVM from "./photos-editor/PhotosEditorVM.ts";
import PhotosEditor from "./photos-editor/PhotosEditor.tsx";

const PageEditListing = ({vm}: PropsPageEditListing) => {

    const [
        content, steps,
        isNextButtonDisabled, isNextButtonLoading,
        isBackButtonDisabled, isBackButtonLoading
    ] = useUpdatesFrom(
        vm.content, vm.steps,
        vm.nextButton.isDisabled, vm.nextButton.isLoading,
        vm.backButton.isDisabled, vm.backButton.isLoading
    );

    return (
        <div className={style.pageEditListing}>

            {
                (content instanceof TitleEditorVM) &&
                <TitleEditor vm={content}/>
            }
            {
                (content instanceof DescriptionEditorVM) &&
                <DescriptionEditor vm={content}/>
            }
            {
                (content instanceof PhotosEditorVM) &&
                <PhotosEditor vm={content}/>
            }
            <Box
                sx={{
                    width: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    position: 'fixed',
                    bottom: 0,
                    left: 0,
                    right: 0
                }}
            >
                <Stepper
                    orientation='horizontal'
                    sx={{
                        "--Step-connectorInset": "10px",
                        "--Step-connectorThickness": "5px",
                        "--Step-gap": "10px",
                        [`& .${stepClasses.completed}`]: {
                            '&::after': {
                                bgcolor: 'primary.500',
                            },
                        }
                    }}
                >
                    {
                        steps.map((isCompleted: boolean, index: number) => {
                            return (
                                <Step
                                    key={index}
                                    completed={isCompleted}
                                />
                            );
                        })
                    }
                </Stepper>

                <Box
                    sx={
                        {
                            display: 'flex',
                            flexDirection: 'row',
                            justifyContent: 'space-between',
                            padding: '10px',
                            paddingLeft: '15px',
                            paddingRight: '15px'
                        }

                    }
                >
                    <Button
                        variant='plain'
                        color='neutral'
                        disabled={isBackButtonDisabled}
                        loading={isBackButtonLoading}
                        onClick={vm.backButton.click}
                    >
                        Back
                    </Button>

                    <Button
                        disabled={isNextButtonDisabled}
                        loading={isNextButtonLoading}
                        onClick={vm.nextButton.click}
                    >
                        Next
                    </Button>
                </Box>
            </Box>
        </div>
    );
}

export default React.memo(PageEditListing);
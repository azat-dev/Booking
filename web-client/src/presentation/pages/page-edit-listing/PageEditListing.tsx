import React from "react";
import PropsPageEditListing from "./props";

import style from './style.module.scss';
import Button from "@mui/joy/Button";
import Box from "@mui/joy/Box";
import {Step, Stepper} from "@mui/joy";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom.ts";
import TitleEditorVM from "./title-editor/TitleEditorVM.ts";
import TitleEditor from "./title-editor/TitleEditor.tsx";

const PageEditListing = ({vm}: PropsPageEditListing) => {

    const [content, isNextButtonEnabled] = useUpdatesFrom(vm.content, vm.isNextButtonEnabled);

    return (
        <div className={style.pageEditListing}>

                {
                    (content instanceof TitleEditorVM) &&
                    <TitleEditor vm={content}/>
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
                    }}
                >
                    <Step/>
                    <Step/>
                    <Step/>
                    <Step/>
                    <Step/>
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
                    <Button variant='plain' color='neutral'>
                        Back
                    </Button>

                    <Button
                        disabled={!isNextButtonEnabled}
                        onClick={vm.moveToNextStep}
                    >
                        Next
                    </Button>
                </Box>
            </Box>
        </div>
    );
}

export default React.memo(PageEditListing);
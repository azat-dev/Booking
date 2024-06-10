import React from "react";
import PropsInitialPhotoInput from "./props";
import {Button, styled, SvgIcon} from "@mui/joy";
import Box from "@mui/joy/Box";

const VisuallyHiddenInput = styled('input')`
    clip: rect(0 0 0 0);
    clip-path: inset(50%);
    height: 1px;
    overflow: hidden;
    position: absolute;
    bottom: 0;
    left: 0;
    white-space: nowrap;
    width: 1px;
`;

const InitialPhotoInput = ({vm}: PropsInitialPhotoInput) => {

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '10px',
                padding: 10,
                borderStyle: 'dashed',
                borderWidth: '1px',
                borderRadius: 'md',
                borderColor: 'primary',
                width: '15vw',
                height: '10vw',
                bgcolor: 'neutral.50'
            }}
        >
            <Button
                startDecorator={
                    <SvgIcon>
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            fill="none"
                            viewBox="0 0 24 24"
                            strokeWidth={1.5}
                            stroke="currentColor"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M12 16.5V9.75m0 0l3 3m-3-3l-3 3M6.75 19.5a4.5 4.5 0 01-1.41-8.775 5.25 5.25 0 0110.233-2.33 3 3 0 013.758 3.848A3.752 3.752 0 0118 19.5H6.75z"
                            />
                        </svg>
                    </SvgIcon>
                }
                onClick={vm.addPhoto}
            >
                Add photos
                {/*<VisuallyHiddenInput type="file" accept=".png, .jpg, .jpeg, .webp"/>*/}
            </Button>
        </Box>
    );
}

export default React.memo(InitialPhotoInput);
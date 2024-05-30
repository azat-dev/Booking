/* tslint:disable */
/* eslint-disable */
/**
 * Users  API
 * Describes the API of Users Endpoint
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
import type { UploadedFileDataDTO } from './UploadedFileDataDTO';
import {
    UploadedFileDataDTOFromJSON,
    UploadedFileDataDTOFromJSONTyped,
    UploadedFileDataDTOToJSON,
} from './UploadedFileDataDTO';

/**
 * 
 * @export
 * @interface GenerateUploadUserPhotoUrlResponseBody
 */
export interface GenerateUploadUserPhotoUrlResponseBody {
    /**
     * 
     * @type {UploadedFileDataDTO}
     * @memberof GenerateUploadUserPhotoUrlResponseBody
     */
    objectPath: UploadedFileDataDTO;
    /**
     * 
     * @type {{ [key: string]: string; }}
     * @memberof GenerateUploadUserPhotoUrlResponseBody
     */
    formData: { [key: string]: string; };
}

/**
 * Check if a given object implements the GenerateUploadUserPhotoUrlResponseBody interface.
 */
export function instanceOfGenerateUploadUserPhotoUrlResponseBody(value: object): boolean {
    if (!('objectPath' in value)) return false;
    if (!('formData' in value)) return false;
    return true;
}

export function GenerateUploadUserPhotoUrlResponseBodyFromJSON(json: any): GenerateUploadUserPhotoUrlResponseBody {
    return GenerateUploadUserPhotoUrlResponseBodyFromJSONTyped(json, false);
}

export function GenerateUploadUserPhotoUrlResponseBodyFromJSONTyped(json: any, ignoreDiscriminator: boolean): GenerateUploadUserPhotoUrlResponseBody {
    if (json == null) {
        return json;
    }
    return {
        
        'objectPath': UploadedFileDataDTOFromJSON(json['objectPath']),
        'formData': json['formData'],
    };
}

export function GenerateUploadUserPhotoUrlResponseBodyToJSON(value?: GenerateUploadUserPhotoUrlResponseBody | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'objectPath': UploadedFileDataDTOToJSON(value['objectPath']),
        'formData': value['formData'],
    };
}

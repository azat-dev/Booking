/* tslint:disable */
/* eslint-disable */
/**
 * Listings API
 * Describes the API of Listings service
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
import type { UploadedFileData } from './UploadedFileData';
import {
    UploadedFileDataFromJSON,
    UploadedFileDataFromJSONTyped,
    UploadedFileDataToJSON,
} from './UploadedFileData';

/**
 * 
 * @export
 * @interface GenerateUploadListingPhotoUrlResponseBody
 */
export interface GenerateUploadListingPhotoUrlResponseBody {
    /**
     * 
     * @type {UploadedFileData}
     * @memberof GenerateUploadListingPhotoUrlResponseBody
     */
    objectPath: UploadedFileData;
    /**
     * 
     * @type {{ [key: string]: string; }}
     * @memberof GenerateUploadListingPhotoUrlResponseBody
     */
    formData: { [key: string]: string; };
}

/**
 * Check if a given object implements the GenerateUploadListingPhotoUrlResponseBody interface.
 */
export function instanceOfGenerateUploadListingPhotoUrlResponseBody(value: object): boolean {
    if (!('objectPath' in value)) return false;
    if (!('formData' in value)) return false;
    return true;
}

export function GenerateUploadListingPhotoUrlResponseBodyFromJSON(json: any): GenerateUploadListingPhotoUrlResponseBody {
    return GenerateUploadListingPhotoUrlResponseBodyFromJSONTyped(json, false);
}

export function GenerateUploadListingPhotoUrlResponseBodyFromJSONTyped(json: any, ignoreDiscriminator: boolean): GenerateUploadListingPhotoUrlResponseBody {
    if (json == null) {
        return json;
    }
    return {
        
        'objectPath': UploadedFileDataFromJSON(json['objectPath']),
        'formData': json['formData'],
    };
}

export function GenerateUploadListingPhotoUrlResponseBodyToJSON(value?: GenerateUploadListingPhotoUrlResponseBody | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'objectPath': UploadedFileDataToJSON(value['objectPath']),
        'formData': value['formData'],
    };
}


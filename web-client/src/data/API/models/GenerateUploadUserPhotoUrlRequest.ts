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
/**
 * 
 * @export
 * @interface GenerateUploadUserPhotoUrlRequest
 */
export interface GenerateUploadUserPhotoUrlRequest {
    /**
     * 
     * @type {string}
     * @memberof GenerateUploadUserPhotoUrlRequest
     */
    idempotentOperationId?: string;
    /**
     * 
     * @type {string}
     * @memberof GenerateUploadUserPhotoUrlRequest
     */
    fileName?: string;
    /**
     * 
     * @type {string}
     * @memberof GenerateUploadUserPhotoUrlRequest
     */
    fileExtension?: string;
    /**
     * 
     * @type {number}
     * @memberof GenerateUploadUserPhotoUrlRequest
     */
    fileSize?: number;
}

/**
 * Check if a given object implements the GenerateUploadUserPhotoUrlRequest interface.
 */
export function instanceOfGenerateUploadUserPhotoUrlRequest(value: object): boolean {
    return true;
}

export function GenerateUploadUserPhotoUrlRequestFromJSON(json: any): GenerateUploadUserPhotoUrlRequest {
    return GenerateUploadUserPhotoUrlRequestFromJSONTyped(json, false);
}

export function GenerateUploadUserPhotoUrlRequestFromJSONTyped(json: any, ignoreDiscriminator: boolean): GenerateUploadUserPhotoUrlRequest {
    if (json == null) {
        return json;
    }
    return {
        
        'idempotentOperationId': json['idempotentOperationId'] == null ? undefined : json['idempotentOperationId'],
        'fileName': json['fileName'] == null ? undefined : json['fileName'],
        'fileExtension': json['fileExtension'] == null ? undefined : json['fileExtension'],
        'fileSize': json['fileSize'] == null ? undefined : json['fileSize'],
    };
}

export function GenerateUploadUserPhotoUrlRequestToJSON(value?: GenerateUploadUserPhotoUrlRequest | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'idempotentOperationId': value['idempotentOperationId'],
        'fileName': value['fileName'],
        'fileExtension': value['fileExtension'],
        'fileSize': value['fileSize'],
    };
}

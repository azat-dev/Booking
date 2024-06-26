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


import * as runtime from '../runtime';
import type {
  SignUpByEmailRequestBody,
  SignUpByEmailResponseBody,
  UserWithSameEmailAlreadyExistsError,
  VerifyEmail400Response,
} from '../models/index';
import {
    SignUpByEmailRequestBodyFromJSON,
    SignUpByEmailRequestBodyToJSON,
    SignUpByEmailResponseBodyFromJSON,
    SignUpByEmailResponseBodyToJSON,
    UserWithSameEmailAlreadyExistsErrorFromJSON,
    UserWithSameEmailAlreadyExistsErrorToJSON,
    VerifyEmail400ResponseFromJSON,
    VerifyEmail400ResponseToJSON,
} from '../models/index';

export interface SignUpByEmailRequest {
    signUpByEmailRequestBody: SignUpByEmailRequestBody;
}

/**
 * 
 */
export class CommandsSignUpApi extends runtime.BaseAPI {

    /**
     * Sign up a new user
     */
    async signUpByEmailRaw(requestParameters: SignUpByEmailRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<SignUpByEmailResponseBody>> {
        if (requestParameters['signUpByEmailRequestBody'] == null) {
            throw new runtime.RequiredError(
                'signUpByEmailRequestBody',
                'Required parameter "signUpByEmailRequestBody" was null or undefined when calling signUpByEmail().'
            );
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        const response = await this.request({
            path: `/api/public/identity/sign-up`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: SignUpByEmailRequestBodyToJSON(requestParameters['signUpByEmailRequestBody']),
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => SignUpByEmailResponseBodyFromJSON(jsonValue));
    }

    /**
     * Sign up a new user
     */
    async signUpByEmail(requestParameters: SignUpByEmailRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<SignUpByEmailResponseBody> {
        const response = await this.signUpByEmailRaw(requestParameters, initOverrides);
        return await response.value();
    }

}

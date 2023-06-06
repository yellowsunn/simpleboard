// https://ckeditor.com/docs/ckeditor5/latest/framework/deep-dive/upload-adapter.html

import {callBoardApi, reAcquireAccessToken} from "@/utils/apiUtils";
import AccessTokenExpiredError from "@/utils/AccessTokenExpiredError";
import {setToken} from "@/utils/tokenUtils";

async function uploadImage(formData) {
  const response = await callBoardApi('POST', '/api/images/article', formData, true, {
    'Content-Type': 'multipart/form-data',
  });
  if (response?.isError) {
    throw new Error(`${response?.data?.message}`)
  }
  return {
    default: response.data
  }
}

export default class UploadAdapter {
  constructor(loader) {
    // The file loader instance to use during the upload.
    this.loader = loader;
  }

  upload() {
    return this.loader.file.then(
      async (file) => {
        const formData = new FormData()
        formData.append('image', file)

        try {
          return await uploadImage(formData)
        } catch (e) {
          if (e instanceof AccessTokenExpiredError) {
            const token = await reAcquireAccessToken()
            await setToken(token.data)
            return await uploadImage(formData)
          }
          throw e
        }
      }
    );
  }
}

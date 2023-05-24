<template>
    <div>
        <CkEditor v-model="editorContent" :editor="editor" :config="editorConfig"/>
    </div>
</template>

<script>
import CKEditor from '@ckeditor/ckeditor5-vue';
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import UploadAdapter from "@/utils/UploadAdapter";

export default {
  name: "TextEditorView",
  components: {'CkEditor': CKEditor.component},
  data() {
    return {
      editorContent: "",
      editor: ClassicEditor,
      editorConfig: {
          language: 'ko',
        toolbar: ['heading', '|', 'imageUpload', 'mediaEmbed', 'link', '|', 'bold', 'italic', '|', 'bulletedList', 'numberedList', 'indent', 'outdent', '|', 'undo', 'redo'],
        table: {
          contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties'],
        },
        image: {
          resize: true,
          toolbar: ['imageStyle:alignLeft', 'imageStyle:alignRight', 'imageStyle:inline', 'imageStyle:side']
        },
        extraPlugins: [function (editor) {
          editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
            return new UploadAdapter(loader);
          }
        }],
      }
    }
  }
}
</script>

<style>
.ck-editor__editable {
    min-height: 300px;
    max-height: 600px;
}
</style>

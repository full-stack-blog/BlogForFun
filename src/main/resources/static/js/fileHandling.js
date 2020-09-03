//==========Create Post FileStack Script ======================
// Set up the picker
const client = filestack.init(FILESTACK_API_KEY);
const options = {
    onUploadDone: updateForm,
    maxSize: 10 * 1024 * 1024,
    accept: 'image/*',
    uploadInBackground: false,
};

const picker = client.picker(options);
// Get references to the DOM elements
const fileInput = document.getElementById('fileupload');
const btn = document.getElementById('picker');
const nameBox = document.getElementById('nameBox');
const urlBox = document.getElementById('urlBox');
// Add our event listeners
btn.addEventListener('click', function (e) {
    e.preventDefault();
    picker.open();

});

function updateForm (result) {
    const fileData = result.filesUploaded[0];
    fileInput.value = fileData.url; //this is the good part
    console.log(fileData.url);
    console.log(fileInput.value);
}


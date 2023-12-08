function showLoader() {
    document.getElementById('loader').style.display = 'block';
    setTimeout(() => {
        if (document.getElementById('loader').style.display == 'block') {
            document.getElementById('loader').style.display = 'none';
        }
    }, 30000); // after 30s hide loader
}

function hideLoader() {
    document.getElementById('loader').style.display = 'none';
}
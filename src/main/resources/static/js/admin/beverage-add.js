document.getElementById('beverage-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const form = event.target;
    const resultMessage = document.getElementById('result-message');

    const sizes = [];
    document.querySelectorAll('.size-form').forEach(formRow => {
        const servingKcalInput = formRow.querySelector('[data-field="servingKcal"]');
        // 칼로리 값이 입력된 경우에만 해당 사이즈 정보를 추가
        if (servingKcalInput && servingKcalInput.value) {
            const sizeData = {
                size: formRow.querySelector('.size-type').value,
                servingKcal: parseInt(servingKcalInput.value) || 0,
                sugarG: parseInt(formRow.querySelector('[data-field="sugarG"]').value) || 0,
                proteinG: parseFloat(formRow.querySelector('[data-field="proteinG"]').value) || 0,
                saturatedFatG: parseFloat(formRow.querySelector('[data-field="saturatedFatG"]').value) || 0,
                sodiumMg: parseInt(formRow.querySelector('[data-field="sodiumMg"]').value) || 0,
                caffeineMg: parseInt(formRow.querySelector('[data-field="caffeineMg"]').value) || 0,
            };
            sizes.push(sizeData);
        }
    });

    const data = {
        beverageName: form.elements.beverageName.value,
        cafeBrand: form.elements.cafeBrand.value,
        imageUrl: form.elements.imageUrl.value,
        sizes: sizes
    };

    resultMessage.textContent = '저장 중...';
    resultMessage.className = 'alert alert-info';

    fetch('/api/admin/beverages', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message || '서버 오류가 발생했습니다.') });
        }
        return response.json();
    })
    .then(data => {
        resultMessage.textContent = data.message || '성공적으로 추가되었습니다.';
        resultMessage.className = 'alert alert-success';
        form.reset();
    })
    .catch(error => {
        resultMessage.textContent = '오류: ' + error.message;
        resultMessage.className = 'alert alert-danger';
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const tableBody = document.getElementById('beverage-table-body');

    fetch('/api/admin/beverages')
        .then(response => {
            if (!response.ok) {
                throw new Error('데이터를 불러오는 데 실패했습니다.');
            }
            return response.json();
        })
        .then(apiResponse => {
            const beverages = apiResponse.data;
            if (!beverages || beverages.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="5" class="text-center">등록된 음료가 없습니다.</td></tr>';
                return;
            }

            let rows = '';
            beverages.forEach(beverage => {
                rows += `
                    <tr>
                        <th scope="row">${beverage.id}</th>
                        <td><img src="${beverage.imageUrl}" alt="${beverage.name}" style="width: 50px; height: 50px; object-fit: cover;"></td>
                        <td>${beverage.name}</td>
                        <td>${beverage.brand}</td>
                        <td>
                            <button class="btn btn-sm btn-info" type="button" data-bs-toggle="collapse" data-bs-target="#collapse-${beverage.id}">
                                영양 정보 보기
                            </button>
                        </td>
                    </tr>
                    <tr class="collapse" id="collapse-${beverage.id}">
                        <td colspan="5">
                            <div class="p-3">
                                ${beverage.sizes.map(size => `
                                    <div class="mb-2">
                                        <strong>${size.size}</strong>: 
                                        칼로리 ${size.nutrition.servingKcal}kcal, 
                                        당류 ${size.nutrition.sugarG}g, 
                                        단백질 ${size.nutrition.proteinG}g, 
                                        포화지방 ${size.nutrition.saturatedFatG}g, 
                                        나트륨 ${size.nutrition.sodiumMg}mg, 
                                        카페인 ${size.nutrition.caffeineMg}mg
                                    </div>
                                `).join('')}
                            </div>
                        </td>
                    </tr>
                `;
            });
            tableBody.innerHTML = rows;

            // Bootstrap Collapse 컴포넌트 초기화
            const collapseElementList = [].slice.call(document.querySelectorAll('.collapse'))
            const collapseList = collapseElementList.map(function (collapseEl) {
              return new bootstrap.Collapse(collapseEl, { toggle: false })
            })
        })
        .catch(error => {
            tableBody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">${error.message}</td></tr>`;
        });
});

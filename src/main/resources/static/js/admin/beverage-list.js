document.addEventListener('DOMContentLoaded', function() {
    const tableBody = document.getElementById('beverage-table-body');
    const filterForm = document.getElementById('filter-form');
    const brandFilter = document.getElementById('brand-filter');
    const keywordSearch = document.getElementById('keyword-search');

    const fetchAndRenderBeverages = (brand = '', keyword = '') => {
        let url = '/api/admin/beverages?';
        const params = new URLSearchParams();
        if (brand) {
            params.append('brand', brand);
        }
        if (keyword) {
            params.append('keyword', keyword);
        }
        url += params.toString();

        tableBody.innerHTML = '<tr><td colspan="5" class="text-center">불러오는 중...</td></tr>';

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('데이터를 불러오는 데 실패했습니다.');
                }
                return response.json();
            })
            .then(apiResponse => {
                const beverages = apiResponse.data;
                if (!beverages || beverages.length === 0) {
                    tableBody.innerHTML = '<tr><td colspan="5" class="text-center">조회된 음료가 없습니다.</td></tr>';
                    return;
                }

                let rows = '';
                beverages.forEach(beverage => {
                    rows += `
                        <tr>
                            <th scope="row">${beverage.id}</th>
                            <td><img src="${beverage.imageUrl}" alt="${beverage.name}" style="width: 50px; height: 50px;"></td>
                            <td>${beverage.name}</td>
                            <td>${cafeBrandMap[beverage.brand]}</td>
                            <td>
                                <button class="btn btn-sm btn-info" type="button" data-bs-toggle="collapse" data-bs-target="#collapse-${beverage.id}">
                                    영양 정보 보기
                                </button>
                            </td>
                        </tr>
                        <tr class="collapse" id="collapse-${beverage.id}">
                            <td colspan="5">
                                <div class="p-3">
                                    ${beverage.sizes.length > 0 ? beverage.sizes.map(size => `
                                        <div class="mb-2">
                                            <strong>${beverageSizeMap[size.size]}</strong>: 
                                            ${size.nutrition.servingMl ? `용량 ${size.nutrition.servingMl}ml, ` : ''}
                                            칼로리 ${size.nutrition.servingKcal}kcal, 
                                            당류 ${size.nutrition.sugarG}g, 
                                            단백질 ${size.nutrition.proteinG}g, 
                                            포화지방 ${size.nutrition.saturatedFatG}g, 
                                            나트륨 ${size.nutrition.sodiumMg}mg, 
                                            카페인 ${size.nutrition.caffeineMg}mg
                                        </div>
                                    `).join('') : '등록된 사이즈 정보가 없습니다.'}
                                </div>
                            </td>
                        </tr>
                    `;
                });
                tableBody.innerHTML = rows;
            })
            .catch(error => {
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">${error.message}</td></tr>`;
            });
    };

    // 폼 제출(검색) 시 이벤트 처리
    filterForm.addEventListener('submit', function(event) {
        event.preventDefault();
        fetchAndRenderBeverages(brandFilter.value, keywordSearch.value);
    });

    // 페이지 로드 시 전체 목록 조회
    fetchAndRenderBeverages();
});

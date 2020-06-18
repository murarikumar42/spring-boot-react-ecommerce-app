package com.ujjaval.ecommerce.commondataservice.service;

import com.ujjaval.ecommerce.commondataservice.dao.sql.categories.*;
import com.ujjaval.ecommerce.commondataservice.dao.sql.images.BrandImagesRepository;
import com.ujjaval.ecommerce.commondataservice.dao.sql.images.CarouselImagesRepository;
import com.ujjaval.ecommerce.commondataservice.dao.sql.images.ClothesTypeImagesRepository;
import com.ujjaval.ecommerce.commondataservice.dao.sql.info.*;
import com.ujjaval.ecommerce.commondataservice.dto.BrandImagesDTO;
import com.ujjaval.ecommerce.commondataservice.dto.ClothesTypeImagesDTO;
import com.ujjaval.ecommerce.commondataservice.entity.sql.images.BrandImages;
import com.ujjaval.ecommerce.commondataservice.entity.sql.images.CarouselImages;
import com.ujjaval.ecommerce.commondataservice.entity.sql.images.ClothesTypeImages;
import com.ujjaval.ecommerce.commondataservice.entity.sql.info.ProductInfo;
import com.ujjaval.ecommerce.commondataservice.model.FilterAttributesComponentResponse;
import com.ujjaval.ecommerce.commondataservice.model.MainScreenResponse;
import com.ujjaval.ecommerce.commondataservice.service.interfaces.CommonDataService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class CommonDataServiceImpl implements CommonDataService {

    @Autowired
    Environment environment;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private AddressInfoRepository addressInfoRepository;

    @Autowired
    private BankInfoRepository bankInfoRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Autowired
    private GenderCategoryRepository genderCategoryRepository;

    @Autowired
    private ClothesTypeCategoryRepository clothesTypeCategoryRepository;

    @Autowired
    private ProductBrandCategoryRepository productBrandCategoryRepository;

    @Autowired
    private BrandImagesRepository brandImagesRepository;

    @Autowired
    private ClothesTypeImagesRepository clothesTypeImagesRepository;

    @Autowired
    private CarouselImagesRepository carouselImagesRepository;

    @Autowired
    private SortByCategoryRepository sortByCategoryRepository;

    @Autowired
    private PriceRangeCategoryRepository priceRangeCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductInfo findAddressById(Integer id) {
        Optional<ProductInfo> result = productInfoRepository.findById(id);

        ProductInfo productInfo = null;

        if (result.isPresent()) {
            productInfo = result.get();
        } else {
            throw new RuntimeException("Address Id is not present " + id);
        }

        return productInfo;
    }

    public String getCurrentHostUrl() throws UnknownHostException {
        return String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(),
                environment.getProperty("local.server.port"));
    }

    public void save() {
//        AddressInfo addressInfo1 = new AddressInfo("2600 bay area blvd.", "Apt. 304", "77058", "Tx", "USA");
//        ContactInfo contactInfo = new ContactInfo("jmiller@gmail.com", "534636453", "345345353", null);
//        BankInfo bankInfo1 = new BankInfo("john", "miller", "Chase bank", "34345834", "0003424653");
//        BankInfo bankInfo2 = new BankInfo("john", "Filler", "Chase bank", "34345834", "0003424653");
//        bankInfo1.setAddressInfo(addressInfo1);
//        bankInfo1.setContactInfo(contactInfo);
//        bankInfo2.setContactInfo(contactInfo);
//        addressInfoRepository.save(addressInfo1);
//        contactInfoRepository.save(contactInfo);
//        bankInfoRepository.save(bankInfo1);
//        bankInfoRepository.save(bankInfo2);


//        ProductMainCategoryInfo productMainCategoryInfo = new ProductMainCategoryInfo("Category1");
//        ProductSubCategoryInfo productSubCategoryInfo = new ProductSubCategoryInfo("SubCategory1");
//        ProductBrandInfo productBrandInfo = new ProductBrandInfo("Brand1");
//
//        ProductInfo productInfo = new ProductInfo(123, "product1", "312423", productBrandInfo,
//                productMainCategoryInfo, productSubCategoryInfo, 34.23, 3, 4, (float) 3.4,
//                false, "image.jpg", null);
//
//        productBrandInfoRepository.save(productBrandInfo);
//        productSubCategoryInfoRepository.save(productSubCategoryInfo);
//        productMainCategoryInfoRepository.save(productMainCategoryInfo);
//        productInfoRepository.save(productInfo);

    }

    public String appendHostUrl(String path) throws UnknownHostException {
        String currentHostUrl = getCurrentHostUrl();
        return String.format("http://%s/web-images/%s", currentHostUrl, path);
    }

    public MainScreenResponse getMainScreenDataList() throws UnknownHostException {

        List<BrandImages> brandList = brandImagesRepository.getAllData();
        Type listType = new TypeToken<List<BrandImagesDTO>>() {}.getType();
        List<BrandImagesDTO> brandDTOList = modelMapper.map(brandList, listType);
        for(BrandImagesDTO info: brandDTOList) {
            info.setFilePath(appendHostUrl(info.getFilePath()));
        }

        List<ClothesTypeImages> clothesTypeList = clothesTypeImagesRepository.getAllData();
        listType = new TypeToken<List<ClothesTypeImagesDTO>>() {}.getType();
        List<ClothesTypeImagesDTO> clothesTypeDTOList = modelMapper.map(clothesTypeList, listType);
        for(ClothesTypeImagesDTO info: clothesTypeDTOList) {
            info.setFilePath(appendHostUrl(info.getFilePath()));
        }

        List<CarouselImages> carouselList = carouselImagesRepository.getAllData();
        for(CarouselImages info: carouselList) {
            info.setFilePath(appendHostUrl(info.getFilePath()));
        }

        return new MainScreenResponse(brandDTOList, clothesTypeDTOList, carouselList);
    }

    public FilterAttributesComponentResponse getFilterAttributesComponentList() {
        return new FilterAttributesComponentResponse(
                productBrandCategoryRepository.getAllData(),
                genderCategoryRepository.getAllData(),
                clothesTypeCategoryRepository.getAllData(),
                sortByCategoryRepository.getAllData(),
                priceRangeCategoryRepository.getAllData()
                );
    }

    public List<ProductInfo> getFilterProductsComponentList(HashMap<String, String> conditionMap)
            throws UnknownHostException {
        List<ProductInfo> productList =  productInfoRepository.getProductInfoByCategories(conditionMap);

        for(ProductInfo info: productList) {
            info.setImageName(appendHostUrl(info.getImageName()));
        }
        return productInfoRepository.getProductInfoByCategories(conditionMap);
    }

}
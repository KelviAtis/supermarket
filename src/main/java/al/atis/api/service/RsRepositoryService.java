package al.atis.api.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


import static al.atis.api.management.AppConstants.ORDER_BY_ASC;
import static al.atis.api.management.AppConstants.ORDER_BY_DESC;

public abstract class RsRepositoryService <T, U> extends RsResponseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final Class<T> entityClass;

    @Autowired
    EntityManager entityManager;

    protected RsRepositoryService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }


    protected void prePersist(T object) throws Exception {
    }

    @PostMapping
    @Transactional
    public ResponseEntity<T> persist(@RequestBody T object) {
        logger.info("persist");
        try {
            prePersist(object);
        } catch (Exception e) {
            logger.error("persist", e);
            return jsonMessageResponse(HttpStatus.BAD_REQUEST, e);
        }

        try {
            entityManager.merge(object);
            if (object == null) {
                logger.error("Failed to create resource: " + object);
                return jsonErrorMessageResponse(object);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(object);
            }
        } catch (Exception e) {
            logger.error("persist", e);
            return jsonErrorMessageResponse(object);
        } finally {
            try {
                postPersist(object);
            } catch (Exception e) {
                logger.error("persist", e);
            }
        }
    }

    protected void postPersist(T object) throws Exception {
    }

    protected void putPrePersist(T object) throws Exception{}
    protected void putPostPersist(T object) throws Exception{}

    protected void deletePrePersist(T object) throws Exception{}
    protected void deletePostPersist(T object) throws Exception{}

    protected void postGetByIdPersist(T object) throws Exception{}

    protected void getListSizePostFetch(T object) throws Exception{}

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<T> updateObject (@RequestBody T object, @PathVariable String id){
        logger.info("prePersistUpdatingObject");
        try {
            putPrePersist(object);
        }  catch (Exception e) {
            logger.error("prePersistUpdatingObject", e);
            return jsonMessageResponse(HttpStatus.BAD_REQUEST, e);
        }

        try {
            if(object == null){
                logger.error("Object is null");
                return jsonErrorMessageResponse(object);
            }else {
                entityManager.merge(object);
                return ResponseEntity.status(HttpStatus.OK).body(object);
            }
        }catch (Exception e){
            logger.error("persistObjectUpdate " + e);
           return jsonErrorMessageResponse(object);
        }

        finally {
          try {
              putPostPersist(object);
          } catch (Exception e) {
              logger.error("postPersistObjectUpdate " + e);
          }
        }
    }


    @DeleteMapping
    @Transactional
    public ResponseEntity<T> deleteObject(@RequestBody T object){
        logger.info("deletingObject");
        try {
            deletePrePersist(object);
        }catch (Exception e){
            return jsonMessageResponse(HttpStatus.BAD_REQUEST, e);
        }

        try {
            if(object == null){
                logger.error("object is null ");
                return jsonErrorMessageResponse(object);
            }else {

                entityManager.remove(object);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(object);
            }
        }catch (Exception e){
            return jsonErrorMessageResponse(e);
        }

        finally {
            try {
                deletePostPersist(object);
            } catch (Exception e) {
                logger.error("postPersistDeleteMethod"+ e);
            }
        }
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<T> getById(@PathVariable String id){
        logger.info("get By Id");
        T objectByID = null;

        try {
            objectByID = entityManager.find(entityClass, id);
            if(objectByID == null ){
                return jsonErrorMessageResponse(HttpStatus.NOT_FOUND);
            }else{
                return ResponseEntity.status(HttpStatus.FOUND).body(objectByID);
            }
        }catch (Exception e){
            return jsonErrorMessageResponse(e);
        }
        finally {
            logger.info("post getById persist");
            try {
                postGetByIdPersist(objectByID);
            } catch (Exception e) {
                return jsonErrorMessageResponse(e);
            }
        }
    }

    @GetMapping("/listSize")
    @Transactional
    public ResponseEntity getListSize(T object){

        logger.info("The entity class: "+ entityClass.toString());
        String name = object.getClass().toString().substring(32,36);
        //String name2 = name.substring(32,36);
        logger.info("The Object name: "+ name);
        String ENTITY = "select b from " + name + " b";

        try {
            Query query = entityManager.createQuery("select b from " + name + " b");
            List resultList = query.getResultList();

            Long sizeOfList = resultList.stream().count();
            logger.info("The size is: " + sizeOfList);
            return ResponseEntity.ok().body(sizeOfList);
        }catch (Exception e){
            return jsonErrorMessageResponse(e);
        }
        finally {
            try {
                logger.info("Post Fetch Method from Get List Size");
                getListSizePostFetch(object);
            }catch (Exception e){

            }
        }
    }

    protected void postFetch(T object) throws Exception {
    }

    @GetMapping
    @Transactional
    public ResponseEntity getList(
            @RequestParam(value = "startRow", required = false, defaultValue = "0") Integer startRow,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", required = false) String orderBy) {

        logger.info("getList");
        try {
            applyFilters();

            long listSize = count();
            List<T> list;
            if (listSize == 0) {
                list = new ArrayList<>();
            } else {
                int currentPage = 0;
                if (pageSize != 0) {
                    currentPage = startRow / pageSize;
                } else {
                    pageSize = Long.valueOf(listSize).intValue();
                }
                TypedQuery<T> search = getSearch(orderBy);
                list = search.setFirstResult(startRow)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
            postList(list);

            return ResponseEntity.ok()
                    .header("startRow", String.valueOf(startRow))
                    .header("pageSize", String.valueOf(pageSize))
                    .header("listSize", String.valueOf(listSize))
                    .body(list);

        } catch (Exception e) {
            logger.error("getList", e);
            return jsonErrorMessageResponse(e);
        }
    }

    protected void postList(List<T> list) throws Exception {}

    private long count(){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        countCriteriaQuery.select(criteriaBuilder.count(countCriteriaQuery.from(getEntityClass())));

        return getEntityManager().createQuery(countCriteriaQuery).getSingleResult();
    }

    private TypedQuery<T> getSearch(String orderBy){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.select(root);

        List<Order> orderList = sort(orderBy, criteriaBuilder, root);
        criteriaQuery.orderBy(orderList);

        TypedQuery<T> search = getEntityManager().createQuery(criteriaQuery);
        return search;
    }

    private List<Order> sort(String orderBy, CriteriaBuilder criteriaBuilder, Root<T> routeRoot) {
        List<Order> orderList = new ArrayList<>();

        List<String> orderByExpresions;
        if (orderBy != null){
            orderByExpresions = fromValueToList(orderBy);
        }
        else {
            orderByExpresions = fromValueToList(getDefaultOrderBy());
        }

        for (String orderByExpresion : orderByExpresions){
            if (orderByExpresion.contains(ORDER_BY_ASC)){
                String property = orderByExpresion.replace(ORDER_BY_ASC, "").trim();
                orderList.add(criteriaBuilder.asc(routeRoot.get(property)));
            }
            else if(orderByExpresion.contains(ORDER_BY_DESC)){
                String property = orderByExpresion.replace(ORDER_BY_DESC, "").trim();
                orderList.add(criteriaBuilder.desc(routeRoot.get(property)));
            }
        }
        return orderList;
    }

    public abstract void applyFilters() throws Exception;

    protected abstract String getDefaultOrderBy();
}

//    @GetMapping("/{id}")
//    @Transactional
//    public ResponseEntity fetch(@PathVariable("id") U id) {
//        logger.info("fetch: " + id);
//
//        try {
//            T t = find(id);
//            if (t == null) {
//                return handleObjectNotFoundRequest(id);
//            } else {
//                try {
//                    postFetch(t);
//                } catch (Exception e) {
//                    logger.errorv(e, "fetch: " + id);
//                }
//                return ResponseEntity.status(HttpStatus.OK).body(t);
//            }
//        } catch (NoResultException e) {
//            logger.error("fetch: " + id, e);
//            return jsonMessageResponse(HttpStatus.NOT_FOUND, id);
//        } catch (Exception e) {
//            logger.error("fetch: " + id, e);
//            return jsonErrorMessageResponse(e);
//        }
//    }

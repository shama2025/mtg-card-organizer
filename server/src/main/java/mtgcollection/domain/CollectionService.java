package mtgcollection.domain;

import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.CollectionRepository;
import mtgcollection.model.Card;
import mtgcollection.model.Collection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    private CardRepository cardRepository;

    private CollectionRepository collectionRepository;

    public CollectionService(CardRepository cardRepository,CollectionRepository collectionRepository) {
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
    }

    public Collection findCollectionByUserId(int userId){return collectionRepository.fetchUserCollection(userId);}

    public List<Card> fetchAllCardsByCollection(int collectionId){
        return cardRepository.fetchAllCards(collectionId);
    }

}

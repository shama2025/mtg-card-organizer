package mtgcollection.domain;

import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.model.Card;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    private CardRepository cardRepository;

    public CollectionService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> fetchAllCardsByUserId(int userId){
        return cardRepository.fetchAllCards(userId);
    }

}

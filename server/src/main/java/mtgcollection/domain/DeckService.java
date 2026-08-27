package mtgcollection.domain;

import mtgcollection.data.interfaces.CardDeckRepository;
import mtgcollection.data.interfaces.CardRepository;
import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.CardDeck;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import mtgcollection.model.card.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService {

    private final DeckRepository deckRepository;

    private final CardDeckRepository cardDeckRepository;

    private final CardRepository cardRepository;

    public DeckService(DeckRepository deckRepository,CardDeckRepository cardDeckRepository,CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.cardDeckRepository = cardDeckRepository;
        this.cardRepository = cardRepository;
    }

    public List<Deck> fetchAllDecksByCollectionId(int collectionId){return deckRepository.fetchAllDecksInACollection(collectionId);}

    public Result<Deck> fetchDeckByDeckId(int deckId){
        Result<Deck> result = new Result<>();
        List<Card> cardList = new ArrayList<>();
        Deck deck = deckRepository.fetchDeckByDeckId(deckId);
        if(deck == null){
            result.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
            return result;
        }
        List<CardDeck> cardDeckList = cardDeckRepository.fetchAllCardDecksFromDeckId(deckId);
        for(CardDeck cardDeck : cardDeckList){
            Card card = cardRepository.fetchCardById(cardDeck.cardId());
            cardList.add(card);
        }
        deck.setCardList(cardList);
        result.setpayload(deck);
        return result;
    }
}

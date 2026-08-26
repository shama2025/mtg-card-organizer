package mtgcollection.domain;

import mtgcollection.data.interfaces.DeckRepository;
import mtgcollection.model.Deck;
import mtgcollection.model.Result;
import mtgcollection.model.ResultType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> fetchAllDecksByCollectionId(int collectionId){return deckRepository.fetchAllDecksInACollection(collectionId);}

    public Result<Deck> fetchDeckByDeckId(int deckId){
        Result<Deck> result = new Result<>();
        Deck deck = deckRepository.fetchDeckByDeckId(deckId);
        if(deck == null){
            result.addErrorMessage("Deck not found.", ResultType.NOT_FOUND);
        }else{
            result.setpayload(deck);
        }
        return result;
    }
}
